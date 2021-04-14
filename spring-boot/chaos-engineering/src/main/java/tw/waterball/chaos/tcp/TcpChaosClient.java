package tw.waterball.chaos.tcp;

import static java.nio.ByteBuffer.allocate;
import static java.util.Arrays.copyOfRange;
import static java.util.stream.Collectors.joining;
import static tw.waterball.chaos.tcp.ProtocolUtils.readStringByContentLength;
import static tw.waterball.chaos.tcp.ProtocolUtils.writeStringByContentLength;
import static tw.waterball.chaos.tcp.TcpChaosServer.OP_INIT_WITH_CHAOS_NAMES;
import static tw.waterball.chaos.tcp.TcpChaosServer.OP_KILLED;
import static tw.waterball.chaos.tcp.TcpChaosServer.OP_SENT_ALIVE_CHAOS_NAMES;
import static tw.waterball.chaos.tcp.TcpChaosServer.PACKET_SIZE;

import lombok.extern.slf4j.Slf4j;
import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.FunValuePacker;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.chaos.core.md5.Md5FunValuePacker;
import tw.waterball.chaos.api.FunValue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
public class TcpChaosClient {
    private final FunValuePacker funValuePacker;
    private final String host;
    private final int port;
    private final Collection<Chaos> chaosCollection;
    private boolean connected;
    private final Collection<ChaosClientListener> listeners = new HashSet<>();
    private ByteBuffer buffer = allocate(PACKET_SIZE);
    private SocketChannel server;

    public TcpChaosClient(FunValuePacker funValuePacker, Collection<Chaos> chaosCollection, String host, int port) {
        this.funValuePacker = funValuePacker;
        this.chaosCollection = chaosCollection;
        this.host = host;
        this.port = port;
    }

    public void addListener(ChaosClientListener listener) {
        listeners.add(listener);
    }

    public void connect() throws IOException {
        log.info("Connecting to {}:{}...", host, port);
        server = SocketChannel.open(new InetSocketAddress(host, port));
        connected = true;
        log.info("Connected");
        initializeWithChaosNames();
        retrieveFunValue();
        sendAliveChaosNames();
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

    private void sendAliveChaosNames() throws IOException {
        String names = chaosCollection.stream().filter(Chaos::isExecuted).map(Chaos::getName).collect(joining(","));
        log.info("Sending the alive chaos names: {}.", names);
        buffer = allocate(PACKET_SIZE);
        buffer.put((byte) OP_SENT_ALIVE_CHAOS_NAMES);
        server.write(writeStringByContentLength(buffer, names).flip());
        log.info("Alive chaos names sent.");
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


    protected void broadcast(Consumer<? super ChaosClientListener> listenerConsumer) {
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

