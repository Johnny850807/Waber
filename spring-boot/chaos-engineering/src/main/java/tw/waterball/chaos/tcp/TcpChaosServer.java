package tw.waterball.chaos.tcp;

import static java.lang.Thread.sleep;
import static java.nio.ByteBuffer.allocate;
import static java.util.Arrays.stream;
import static java.util.Collections.synchronizedMap;
import static java.util.Collections.synchronizedSet;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static tw.waterball.chaos.tcp.ProtocolUtils.readStringByContentLength;
import static tw.waterball.chaos.tcp.ProtocolUtils.writeStringByContentLength;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.ChaosEngineListener;
import tw.waterball.chaos.api.FunValue;
import tw.waterball.chaos.api.FunValuePacker;
import tw.waterball.chaos.core.md5.Md5FunValue;
import tw.waterball.chaos.core.md5.Md5FunValuePacker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * TODO: clear `chaosNameToClientMap` when a client exits
 *
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public class TcpChaosServer implements ChaosEngineListener {
    public static final int OP_INIT_WITH_CHAOS_NAMES = 30;
    public static final int OP_SENT_ALIVE_CHAOS_NAMES = 31;
    public static final int OP_KILLED = 45;
    public static final int OP_FUN_VALUE_EFFECT = 101;
    public static final int PACKET_SIZE = 10240;
    private final String host;
    private final int port;
    private final FunValuePacker packer;
    private boolean running;
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private ByteBuffer buffer;
    private final Collection<ChaosServerListener> listeners = synchronizedSet(new HashSet<>());
    private final Map<String, Set<SocketChannel>> chaosNameToClientMap = synchronizedMap(new HashMap<>());
    private byte[] funValueBytes;

    public TcpChaosServer(FunValuePacker packer, String host, int port) {
        this.packer = packer;
        this.host = host;
        this.port = port;
    }

    public void addListener(ChaosServerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onFunValueInitialized(FunValue funValue) {
        this.funValueBytes = packer.write(funValue);
        for (SocketChannel client : getClients()) {
            try {
                effectFunValue(client);
            } catch (IOException e) {
                log.error("Error during sending the fun-value", e);
            }
        }
    }

    @Override
    public void onChaosKilled(Chaos chaos) {
        this.kill(chaos.getName());
    }

    public void start() throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(host, port));
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        running = true;
        new Thread(this::eventLoop).start();
    }

    private void eventLoop() {
        log.info("The TCP Chaos server in running on port {}.", port);
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
                        handleOperation(key);
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
    private synchronized void handleOperation(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        buffer = allocate(PACKET_SIZE);
        client.read(buffer);
        int opCode = -1;
        try {
            opCode = buffer.flip().get();
            if (opCode == OP_INIT_WITH_CHAOS_NAMES) {
                initializeClientWithChaosNames(client);
                if (funValueBytes != null) {
                    effectFunValue(client);
                }
            } else if (opCode == OP_SENT_ALIVE_CHAOS_NAMES) {
                claimAliveChaos(client);
            } else {
                log.error("Unrecognizable OP Code: {}.", opCode);
                kickClientOff(client);
            }
        } catch (BufferUnderflowException e) {
            log.error("OPCODE: {}, Error: {}", opCode, e.getMessage());
            kickClientOff(client);
        }
    }

    private void initializeClientWithChaosNames(SocketChannel client) throws IOException {
        String chaosNamesSplitByComma = readStringByContentLength(buffer);
        String[] chaosNames = chaosNamesSplitByComma.split("\\s*,\\s*");
        log.info("New chaos registered: {}.", String.join(", ", chaosNames));
        mapChaosToClient(chaosNames, client);
        try {
            broadcast(l -> l.onChaosRegistered(chaosNames));
        } catch (IllegalArgumentException err) {
            log.error(err.getMessage());
        }
    }

    private Collection<SocketChannel> getClients() {
        return chaosNameToClientMap.values()
                .stream().flatMap(Collection::stream)
                .collect(toUnmodifiableSet());
    }

    private void effectFunValue(SocketChannel client) throws IOException {
        buffer = allocate(PACKET_SIZE);
        buffer.put((byte) OP_FUN_VALUE_EFFECT);
        buffer.put(funValueBytes);
        client.write(buffer.flip());
    }

    private void claimAliveChaos(SocketChannel client) {
        String chaosNamesSplitByCommas = readStringByContentLength(buffer);
        String[] chaosNames = chaosNamesSplitByCommas.split("\\s*,\\s*");
        log.info("Alive chaos claimed: {}.", String.join(", ", chaosNames));
        mapChaosToClient(chaosNames, client);
        broadcast(l -> l.onChaosClaimedAlive(chaosNames));
    }

    private void mapChaosToClient(String[] chaosNames, SocketChannel client) {
        stream(chaosNames).forEach(name ->
                chaosNameToClientMap.computeIfAbsent(name, k -> new HashSet<>()).add(client));
    }

    private void kickClientOff(SocketChannel client) {
        chaosNameToClientMap.values().forEach(clients -> clients.remove(client));
        if (client.isConnected()) {
            try (client /*close if*/) {
                String ip = client.getRemoteAddress().toString();
                log.error("The client {} is kicked off.", ip);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    protected synchronized void kill(String chaosName) {
        log.info("Killing {}...", chaosName);
        // [DRUNK CODE] The delay is necessary, as the client won't be able to consume the data so instantly
        // TODO: figure out a better way to solve this
        sleep(800);
        for (SocketChannel client : chaosNameToClientMap.get(chaosName)) {
            try {
                buffer = allocate(PACKET_SIZE);
                client.write(writeStringByContentLength(buffer.put((byte) OP_KILLED), chaosName).flip());
                log.info("Signal the kill message ({} is killed) to {}", chaosName, client.getRemoteAddress().toString());
            } catch (IOException e1) {
                log.info("Error", e1);
                try {
                    client.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } finally {
                    chaosNameToClientMap.values().forEach(clients -> clients.remove(client));
                }
            }
        }
    }

    private void broadcast(Consumer<? super ChaosServerListener> listenerConsumer) {
        listeners.forEach(listenerConsumer);
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) throws IOException {
        TcpChaosServer server = new TcpChaosServer(new Md5FunValuePacker(), "localhost", 9999);
        server.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();
            server.onFunValueInitialized(new Md5FunValue(next));
        }
    }
}
