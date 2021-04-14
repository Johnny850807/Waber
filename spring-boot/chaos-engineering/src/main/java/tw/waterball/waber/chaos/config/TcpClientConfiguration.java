package tw.waterball.waber.chaos.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.waber.chaos.api.Chaos;
import tw.waterball.waber.chaos.api.ChaosEngine;
import tw.waterball.waber.chaos.api.FunValue;
import tw.waterball.waber.chaos.api.FunValuePacker;
import tw.waterball.waber.chaos.core.ChaosEngineImpl;
import tw.waterball.waber.chaos.core.md5.Md5FunValue;
import tw.waterball.waber.chaos.core.md5.Md5FunValuePacker;
import tw.waterball.waber.chaos.tcp.TcpChaosClient;

import java.util.Collection;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Configuration
public class TcpClientConfiguration {

    @Bean
    public FunValue funValue(@Value("${chaos.fun-value}") String funValue) {
        return new Md5FunValue(funValue);
    }

    @Bean
    public FunValuePacker funValuePacker() {
        return new Md5FunValuePacker();
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ChaosEngine waberChaosEngine(Collection<Chaos> chaos,
                                        Collection<ChaosEngine.Listener> listeners, FunValue funValue) {
        ChaosEngine chaosEngine = new ChaosEngineImpl(chaos);
        listeners.forEach(chaosEngine::addListener);
        return chaosEngine;
    }

    @Bean
    public TcpChaosClient tcpChaosClient(FunValuePacker funValuePacker,
                                         @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                                 Collection<Chaos> chaosCollection,
                                         @Value("${chaos.host}") String host,
                                         @Value("${chaos.port}") int port) {
        return new TcpChaosClient(funValuePacker, chaosCollection, host, port);
    }

    @Bean
    public TcpChaosClient.Listener tcpChaosClientListener(ChaosEngine chaosEngine) {
        return new TcpChaosClient.Listener() {
            @Override
            public void onFunValueInitialized(FunValue funValue) {
                log.debug("Starting the chaos engine...");
                chaosEngine.start(funValue);
                log.debug("Chaos engine started.");
            }

            @Override
            public void onChaosKilled(Chaos chaos) {
                log.debug("The chaos '{}' is killed.", chaos.getName());
                chaosEngine.kill(chaos.getName());
            }
        };
    }
}
