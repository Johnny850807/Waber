package tw.waterball.waber.chaos.tcp;

import static java.nio.ByteBuffer.allocate;
import static java.util.Arrays.copyOfRange;
import static java.util.stream.Collectors.joining;
import static tw.waterball.waber.chaos.tcp.ProtocolUtils.readStringByContentLength;
import static tw.waterball.waber.chaos.tcp.ProtocolUtils.writeStringByContentLength;
import static tw.waterball.waber.chaos.tcp.TcpChaosServer.OP_INIT_WITH_CHAOS_NAMES;
import static tw.waterball.waber.chaos.tcp.TcpChaosServer.OP_KILLED;
import static tw.waterball.waber.chaos.tcp.TcpChaosServer.PACKET_SIZE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tw.waterball.waber.chaos.api.Chaos;
import tw.waterball.waber.chaos.api.FunValue;
import tw.waterball.waber.chaos.api.FunValuePacker;
import tw.waterball.waber.chaos.core.md5.Md5Chaos;
import tw.waterball.waber.chaos.core.md5.Md5FunValuePacker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Component
public class TcpChaosClient {
    private final FunValuePacker funValuePacker;
    private final String host;
    private final int port;
    private final Collection<Chaos> chaosCollection;
    private boolean connected;
    private final Collection<Listener> listeners = new HashSet<>();
    private ByteBuffer buffer = allocate(PACKET_SIZE);
    private SocketChannel server;

    public interface Listener {
        void onFunValueInitialized(FunValue funValue);

        void onChaosKilled(Chaos chaos);
    }

    public TcpChaosClient(FunValuePacker funValuePacker, Collection<Chaos> chaosCollection, String host, int port) {
        this.funValuePacker = funValuePacker;
        this.chaosCollection = chaosCollection;
        this.host = host;
        this.port = port;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void connect() throws IOException {
        log.info("Connecting to {}:{}...", host, port);
        server = SocketChannel.open(new InetSocketAddress(host, port));
        connected = true;
        log.info("Connected");
        initializeWithChaosNames();
        retrieveFunValue();
        listenToInput();
    }

    private void initializeWithChaosNames() throws IOException {
        String names = chaosCollection.stream().map(Chaos::getName).collect(joining(", "));
        log.info("Initializing Chaos client with a set of chaos {}...", names);
        buffer = allocate(PACKET_SIZE);
        buffer.put((byte) OP_INIT_WITH_CHAOS_NAMES);
        buffer = writeStringByContentLength(buffer, names).flip();
        server.write(buffer);
        log.info("Initialized.");
    }

    private void retrieveFunValue() throws IOException {
        log.info("Retrieving the fun value...");
        buffer = allocate(PACKET_SIZE);
        server.read(buffer);
        FunValue funValue = funValuePacker.read(buffer.array());
        broadcast(l -> l.onFunValueInitialized(funValue));
        log.info("Fun value retrieved: {}.", funValue);

    }

    public void disconnect() throws IOException {
        server.close();
    }

    private void listenToInput() {
        new Thread(() -> {
            try {
                while (connected) {
                    buffer.clear();
                    server.read(buffer);
                    int opCode = buffer.flip().get();
                    if (opCode == OP_KILLED) {
                        killChaos();
                    } else {
                        log.error("Unrecognizable OP Code: {}.", opCode);
                    }
                }
            } catch (IOException err) {
                connected = false;
            }
        }).start();
    }

    private void killChaos() {
        String chaosName = readStringByContentLength(buffer);
        log.info("Killing the chaos: {}.", chaosName);
        chaosCollection.stream()
                .filter(chaos -> chaos.getName().equals(chaosName))
                .forEach(chaos -> {
                    chaos.kill();
                    log.info("The chaos {} is killed.", chaos.getName());
                    broadcast(listener -> listener.onChaosKilled(chaos));
                });
    }


    protected void broadcast(Consumer<? super Listener> listenerConsumer) {
        listeners.forEach(listenerConsumer);
    }

    public static void main(String[] args) throws IOException {
        new TcpChaosClient(new Md5FunValuePacker(), Arrays.asList(new Md5Chaos() {
                                                                      @Override
                                                                      protected Criteria criteria() {
                                                                          return null;
                                                                      }

                                                                      @Override
                                                                      public String getName() {
                                                                          return "A";
                                                                      }
                                                                  },
                new Md5Chaos() {
                    @Override
                    protected Criteria criteria() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return "B";
                    }
                }), "localhost", 9999).connect();
    }
}

