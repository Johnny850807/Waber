package tw.waterball.waber.chaos.springboot.chaos.tcp;

import static tw.waterball.waber.chaos.springboot.chaos.tcp.TcpChaosServer.OP_INIT_WITH_NAME;
import static tw.waterball.waber.chaos.springboot.chaos.tcp.TcpChaosServer.OP_KILLED;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Component
public class TcpChaosClient {
    private final String chaosName;
    private final String host;
    private final int port;
    private boolean connected;
    private final Collection<Listener> listeners = new HashSet<>();
    private ByteBuffer buffer;
    private SocketChannel server;

    public interface Listener {
        void onKilled();
    }

    public TcpChaosClient(String chaosName, String host, int port) {
        this.chaosName = chaosName;
        this.host = host;
        this.port = port;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void connect() throws IOException {
        log.info("[{}] Connecting to {}:{}...", chaosName, host, port);
        server = SocketChannel.open(new InetSocketAddress(host, port));
        connected = true;
        log.info("[{}] Connected", chaosName);
        initializeWithName();
        listenToInput();
    }

    private void initializeWithName() throws IOException {
        log.info("[{}] Initializing Chaos client with name {}...", chaosName, chaosName);
        buffer = ByteBuffer.allocate(256);
        byte[] nameBytes = chaosName.getBytes(StandardCharsets.UTF_8);
        server.write(buffer.put((byte) OP_INIT_WITH_NAME)
                .put((byte) nameBytes.length)
                .put(nameBytes)
                .flip());
        log.info("[{}] Initialized.", chaosName);
    }

    public void disconnect() throws IOException {
        server.close();
    }

    private void listenToInput() {
        new Thread(() -> {
            Thread.currentThread().setName(chaosName);
            try {
                while (connected) {
                    buffer = ByteBuffer.allocate(1);
                    server.read(buffer);
                    int opCode = buffer.get(0);
                    if (opCode == OP_KILLED) {
                        log.error("[{}] Getting killed.", chaosName);
                        broadcast(Listener::onKilled);
                        disconnect();
                    } else {
                        log.error("[{}] Unrecognizable OP Code: {}.", chaosName, opCode);
                    }
                }
            } catch (IOException err) {
                connected = false;
            }
        }).start();
    }


    protected void broadcast(Consumer<? super Listener> listenerConsumer) {
        listeners.forEach(listenerConsumer);
    }

}

