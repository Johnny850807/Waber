package tw.waterball.waber.chaos.springboot.chaos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.waber.chaos.springboot.chaos.tcp.TcpChaosServer;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class TcpServerConfiguration {

    @Bean
    public TcpChaosServer tcpChaosServer(@Value("${chaos.host}") String host,
                                         @Value("${chaos.port}") int port) {
        return new TcpChaosServer(host, port);
    }
}
