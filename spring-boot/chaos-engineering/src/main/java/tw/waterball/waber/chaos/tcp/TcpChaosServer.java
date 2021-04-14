package tw.waterball.waber.chaos.tcp;

import static java.nio.ByteBuffer.allocate;
import static java.util.Arrays.stream;
import static tw.waterball.waber.chaos.tcp.ProtocolUtils.readStringByContentLength;
import static tw.waterball.waber.chaos.tcp.ProtocolUtils.writeStringByContentLength;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tw.waterball.waber.chaos.api.Chaos;
import tw.waterball.waber.chaos.api.ChaosEngine;
import tw.waterball.waber.chaos.core.md5.Md5FunValue;
import tw.waterball.waber.chaos.core.md5.Md5FunValuePacker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public class TcpChaosServer implements ChaosEngine.Listener {
    public static final int OP_INIT_WITH_CHAOS_NAMES = 30;
    public static final int OP_KILLED = 45;
    public static final int PACKET_SIZE = 2048;
    private final String host;
    private final int port;
    private boolean running;
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private ByteBuffer buffer;
    private final ByteBuffer funValueBytes;
    private final Collection<Listener> listeners = new HashSet<>();
    private final Map<String, SocketChannel> chaosNameToClientMap = new HashMap<>();

    public TcpChaosServer(byte[] funValueBytes, String host, int port) {
        this.funValueBytes = allocate(PACKET_SIZE).put(funValueBytes);
        this.host = host;
        this.port = port;
    }

    @Override
    public void onChaosKilled(Chaos chaos) {
        this.kill(chaos.getName());
    }

    public interface Listener {
        void onChaosClientRegistered(String[] chaosNames);

        void onChaosClientExited(String chaosName);
    }

    public void start() throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(host, port));
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        running = true;
        eventLoop();
    }

    private void eventLoop() {
        log.info("Running.");
        while (running) {
            try {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        register();
                    }

                    if (key.isReadable()) {
                        initializeClient(key);
                    }
                    iter.remove();
                }
            } catch (IOException err) {
                log.error("Error", err);
            }
        }
        log.info("Stopped.");
    }

    private void register() throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    @SneakyThrows
    private synchronized void initializeClient(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        buffer = allocate(PACKET_SIZE);
        client.read(buffer);
        int opCode = buffer.flip().get();
        if (opCode == OP_INIT_WITH_CHAOS_NAMES) {
            try {
                initializeClientWithChaosNames(client);
                sendFunValue(client);
            } catch (IOException err) {
                kickClientOff(client);
            }
        } else {
            log.error("Unrecognizable OP Code: {}.", opCode);
            kickClientOff(client);
        }
    }

    private void initializeClientWithChaosNames(SocketChannel client) throws IOException {
        String chaosNamesSplitByComma = readStringByContentLength(buffer);
        String[] chaosNames = chaosNamesSplitByComma.split("\\s*,\\s*");
        log.info("New chaos registered: {}.", String.join(", ", chaosNames));
        stream(chaosNames).forEach(name -> chaosNameToClientMap.put(name, client));
        broadcast(l -> l.onChaosClientRegistered(chaosNames));
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stream(chaosNames).forEach(this::kill);
        }).start();
    }

    private void sendFunValue(SocketChannel client) throws IOException {
        funValueBytes.flip();
        client.write(funValueBytes);
    }

    private void kickClientOff(SocketChannel client) {
        try (client) {
            String ip = client.getRemoteAddress().toString();
            log.error("The client {} is kicked off.", ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void kill(String chaosName) {
        buffer = allocate(PACKET_SIZE);
        SocketChannel client = chaosNameToClientMap.get(chaosName);
        try {
            log.info("Killing {}...", chaosName);
            client.write(writeStringByContentLength(buffer.put((byte) OP_KILLED), chaosName).flip());
            log.info("{} is killed", chaosName);
        } catch (IOException e1) {
            log.info("Error", e1);
            try {
                client.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            } finally {
                chaosNameToClientMap.entrySet().removeIf(e -> e.getValue() == client);
            }
        } finally {
            broadcast(l -> l.onChaosClientExited(chaosName));
        }
    }

    private void broadcast(Consumer<? super Listener> listenerConsumer) {
        listeners.forEach(listenerConsumer);
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) throws IOException {
        new TcpChaosServer(new Md5FunValuePacker().write(new Md5FunValue("Hello World")),
                "localhost", 9999).start();
    }
}
