package tw.waterball.waber.chaos.springboot.chaos.tcp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public class TcpChaosServer {
    public static final int OP_INIT_WITH_NAME = 30;
    public static final int OP_KILLED = 45;
    private final String host;
    private final int port;
    private boolean running;
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private ByteBuffer buffer;
    private final Map<String, SocketChannel> nameToClient = new HashMap<>();

    public TcpChaosServer(String host, int port) {
        this.host = host;
        this.port = port;
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
                        read(key);
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
    private synchronized void read(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        byte[] bytes = buffer.array();
        int opCode = bytes[0];
        if (opCode == OP_INIT_WITH_NAME) {
            // [OPCODE (1) | N := Content Length (1) | Name (N) ]
            int contentLength = bytes[1];
            String name = new String(bytes, 2, contentLength);
            log.info("New chaos registered: {}.", name);
            nameToClient.put(name, client);
            kill(name);
        } else {
            log.error("Unrecognizable OP Code: {}.", opCode);
            nameToClient.entrySet().removeIf((e) -> e.getValue() == client);
            String ip = client.getRemoteAddress().toString();
            client.close();
            log.error("The client {} is kicked off.", ip);
        }
    }

    public synchronized void kill(String chaosName) {
        buffer = ByteBuffer.allocate(1);
        buffer.put((byte) OP_KILLED).flip();
        try {
            log.info("Killing {}...", chaosName);
            nameToClient.get(chaosName).write(buffer);
            log.info("{} is killed", chaosName);
        } catch (IOException e) {
            log.info("Error", e);
        }
    }

    public void stop() {
        running = false;
    }

}
