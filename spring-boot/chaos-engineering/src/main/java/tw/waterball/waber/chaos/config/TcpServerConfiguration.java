package tw.waterball.waber.chaos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.waber.chaos.api.Chaos;
import tw.waterball.waber.chaos.api.ChaosEngine;
import tw.waterball.waber.chaos.api.FunValue;
import tw.waterball.waber.chaos.api.FunValuePacker;
import tw.waterball.waber.chaos.core.ChaosEngineImpl;
import tw.waterball.waber.chaos.core.md5.Md5Chaos;
import tw.waterball.waber.chaos.core.md5.Md5FunValue;
import tw.waterball.waber.chaos.core.md5.Md5FunValuePacker;
import tw.waterball.waber.chaos.tcp.TcpChaosServer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class TcpServerConfiguration {

    @Bean
    public FunValue funValue(@Value("${chaos.fun-value}") String funValue) {
        return new Md5FunValue(funValue);
    }

    @Bean
    public FunValuePacker funValuePacker() {
        return new Md5FunValuePacker();
    }

    @Bean
    public TcpChaosServer tcpChaosServer(@Value("${chaos.host}") String host,
                                         @Value("${chaos.port}") int port,
                                         FunValue funValue,
                                         FunValuePacker funValuePacker) {
        return new TcpChaosServer(funValuePacker.write(funValue), host, port);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ChaosEngine waberChaosEngine(Collection<Chaos> chaos, Collection<ChaosEngine.Listener> listeners) {
        ChaosEngine chaosEngine = new ChaosEngineImpl(chaos);
        listeners.forEach(chaosEngine::addListener);
        return chaosEngine;
    }

    @Bean
    public TcpChaosServer.Listener tcpChaosServerListener(ChaosEngine chaosEngine) {
        return new TcpChaosServer.Listener() {
            @Override
            public void onChaosClientRegistered(String[] chaosNames) {
                Arrays.stream(chaosNames)
                        .forEach(name -> {
                            chaosEngine.addChaos(new Md5Chaos() {  /*Mock Chaos*/
                                @Override
                                protected Criteria criteria() {
                                    throw new UnsupportedOperationException("No matching procedure in the server side's engine.");
                                }

                                @Override
                                public String getName() {
                                    return name;
                                }
                            });

                        });
            }

            @Override
            public void onChaosClientExited(String chaosName) {
                chaosEngine.removeChaosByName(chaosName);
            }
        };
    }
}
