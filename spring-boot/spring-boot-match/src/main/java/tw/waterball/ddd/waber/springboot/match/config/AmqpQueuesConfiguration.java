package tw.waterball.ddd.waber.springboot.match.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class AmqpQueuesConfiguration {
    public static final String MATCHES_COMPLETE = "/matches/complete";

    @Bean
    public Queue matchCompleteQueue() {
        return new Queue(MATCHES_COMPLETE);
    }
}
