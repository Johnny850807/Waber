package tw.waterball.ddd.waber.springboot.trip.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.TripStateChangedEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class RabbitEventBusConfiguration {
    public static final String EVENTS_EXCHANGE = "events";

    @Bean
    public DirectExchange eventsExchange() {
        return new DirectExchange(EVENTS_EXCHANGE);
    }

    @Bean
    public EventBus.Subscriber rabbitEventBusSubscriber(AmqpTemplate amqpTemplate) {
        return event -> {
            if (TripStateChangedEvent.NAME.equals(event.getName())) {
                amqpTemplate.convertAndSend(EVENTS_EXCHANGE, "/trips/state/change", event);
            }
        };
    }
}
