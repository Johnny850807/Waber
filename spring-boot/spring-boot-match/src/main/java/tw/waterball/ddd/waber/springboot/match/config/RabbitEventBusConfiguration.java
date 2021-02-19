package tw.waterball.ddd.waber.springboot.match.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class RabbitEventBusConfiguration {

    @Bean
    public EventBus.Subscriber rabbitEventBusSubscriber(AmqpTemplate amqpTemplate) {
        return event -> {
            if (MatchCompleteEvent.EVENT_NAME.equals(event.getName())) {
                amqpTemplate.convertAndSend(AmqpQueuesConfiguration.MATCHES_COMPLETE, event);
            }
        };
    }
}
