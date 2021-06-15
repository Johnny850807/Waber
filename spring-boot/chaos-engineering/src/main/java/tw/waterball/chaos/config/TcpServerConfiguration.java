package tw.waterball.chaos.config;

import static java.util.Arrays.stream;
import static java.util.Collections.synchronizedSet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.ChaosEngine;
import tw.waterball.chaos.api.ChaosEngineListener;
import tw.waterball.chaos.api.FunValue;
import tw.waterball.chaos.api.FunValuePacker;
import tw.waterball.chaos.core.ChaosEngineImpl;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.chaos.core.md5.Md5FunValue;
import tw.waterball.chaos.core.md5.Md5FunValuePacker;
import tw.waterball.chaos.tcp.ChaosServerListener;
import tw.waterball.chaos.tcp.TcpChaosServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Configuration
public class TcpServerConfiguration {

    @Bean
    public FunValuePacker funValuePacker() {
        return new Md5FunValuePacker();
    }

    @Bean
    public TcpChaosServer tcpChaosServer(@Value("${chaos.host}") String host,
                                         @Value("${chaos.port}") int port,
                                         FunValuePacker funValuePacker) {
        return new TcpChaosServer(funValuePacker, host, port);
    }

    @Bean
    public ChaosEngine waberChaosEngine(Collection<Chaos> chaos, Collection<ChaosEngineListener> listeners) {
        ChaosEngine chaosEngine = new ChaosEngineImpl(chaos);
        listeners.forEach(chaosEngine::addListener);
        return chaosEngine;
    }

    @Bean
    public SmartInitializingSingleton addListenerToChaosServer(ChaosEngine chaosEngine,
                                                               TcpChaosServer chaosServer) {
        Set<String> aliveness = synchronizedSet(new HashSet<>());
        return () -> {
            chaosServer.addListener(new ChaosServerListener() {
                @Override
                public void onChaosRegistered(String[] chaosNames) {
                    stream(chaosNames).forEach(name -> chaosEngine.addChaos(new Md5Chaos() {  /*Mock Chaos*/
                        @Override
                        public void execute(FunValue funValue) {
                            // reset the aliveness claim on each re-execution
                            aliveness.remove(getName());
                            super.execute(funValue);
                        }

                        @Override
                        protected void onKilled() {
                            aliveness.remove(this.getName());
                        }

                        @Override
                        protected Criteria criteria() {
                            // at the server's side, all chaos are marked alive until they're killed
                            return always();
                        }

                        @Override
                        public String getName() {
                            return name;
                        }

                        @Override
                        public boolean isAlive() {
                            return aliveness.contains(getName());
                        }
                    }));
                }

                @Override
                public void onChaosClaimedAlive(String[] chaosNames) {
                    aliveness.addAll(Arrays.asList(chaosNames));
                }
            });
            try {
                chaosServer.start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}