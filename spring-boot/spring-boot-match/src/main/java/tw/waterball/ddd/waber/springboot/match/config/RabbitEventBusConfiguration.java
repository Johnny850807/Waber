package tw.waterball.ddd.waber.springboot.match.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.MatchCompleteEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class RabbitEventBusConfiguration {
    public static final String EVENTS_EXCHANGE = "events";

    @Bean
    public Exchange eventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE);
    }

    @Bean
    public EventBus.Subscriber rabbitEventBusSubscriber(AmqpTemplate amqpTemplate) {
        return event -> {
            if (MatchCompleteEvent.EVENT_NAME.equals(event.getName())) {
                amqpTemplate.convertAndSend(EVENTS_EXCHANGE, "matches/complete", event);
            }
        };
    }
}
