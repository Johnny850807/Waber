package tw.waterball.ddd.waber.springboot.user.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class RabbitEventBusConfiguration {

    public static final String EVENTS_EXCHANGE = "events";



    @Bean
    public EventBus.Subscriber rabbitEventBusSubscriber(AmqpTemplate amqpTemplate) {
        return event -> {
            if (UserLocationUpdatedEvent.NAME.equals(event.getName())) {
                amqpTemplate.convertAndSend(EVENTS_EXCHANGE, "/users/location", event);
            }
        };
    }
}
