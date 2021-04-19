package tw.waterball.chaos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.ChaosEngine;
import tw.waterball.chaos.api.ChaosEngineListener;
import tw.waterball.chaos.api.ChaosMiskillingException;
import tw.waterball.chaos.api.FunValue;
import tw.waterball.chaos.api.FunValuePacker;
import tw.waterball.chaos.core.ChaosEngineImpl;
import tw.waterball.chaos.core.md5.Md5FunValuePacker;
import tw.waterball.chaos.tcp.ChaosClientListener;
import tw.waterball.chaos.tcp.TcpChaosClient;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Configuration
public class TcpClientConfiguration {

    @Bean
    public FunValuePacker funValuePacker() {
        return new Md5FunValuePacker();
    }

    @Bean
    public ChaosEngine waberChaosEngine(Collection<Chaos> chaos,
                                        Collection<ChaosEngineListener> listeners) {
        ChaosEngine chaosEngine = new ChaosEngineImpl(chaos);
        listeners.forEach(chaosEngine::addListener);
        return chaosEngine;
    }

    @Bean
    public TcpChaosClient tcpChaosClient(FunValuePacker funValuePacker,
                                                 Collection<Chaos> chaosCollection,
                                         @Value("${chaos.host}") String host,
                                         @Value("${chaos.port}") int port) {
        return new TcpChaosClient(funValuePacker, chaosCollection, host, port);
    }



    @Bean
    public SmartInitializingSingleton runChaosClient(ChaosEngine chaosEngine, TcpChaosClient chaosClient) {
        return () -> {
            try {
                chaosClient.addListener(new ChaosClientListener() {
                    @Override
                    public void onFunValueInitialized(FunValue funValue) {
                        log.debug("Starting the chaos engine...");
                        chaosEngine.start(funValue);
                        log.debug("Chaos engine started.");
                    }

                    @Override
                    public void onChaosKilled(Chaos chaos) {
                        try {
                            log.debug("Killing the chaos '{}'...", chaos.getName());
                            chaosEngine.kill(chaos.getName());
                        } catch (ChaosMiskillingException ignored) { }
                    }
                });
                chaosClient.connect();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
