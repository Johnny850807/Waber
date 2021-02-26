package tw.waterball.ddd.waber.springboot.broker.brokers;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class AmqpConfiguration {
    public static final String EVENTS_EXCHANGE = "events";

    @Bean
    public DirectExchange eventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE);
    }

}
