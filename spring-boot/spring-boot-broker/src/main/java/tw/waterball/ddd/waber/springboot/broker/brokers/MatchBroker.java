package tw.waterball.ddd.waber.springboot.broker.brokers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import tw.waterball.ddd.events.MatchCompleteEvent;


/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class MatchBroker {
    public static final String ROUTING_KEY = "/matches/complete";
    public static final String QUEUE_NAME = "waber-broker:MatchBroker";
    private final SimpMessagingTemplate simpMessaging;

    @Bean
    public Binding bindMatchQueueToEvents(@Qualifier("eventsExchange") DirectExchange exchange,
                                 @Qualifier("matchQueue") Queue queue) {
        log.info("Exchange binding: queue={} -> exchange={} routingKey={}", queue.getName(), exchange.getName(), ROUTING_KEY);
        return BindingBuilder.bind(queue)
                .to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue matchQueue() {
        return new Queue(QUEUE_NAME);
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void listenToMatch(MatchCompleteEvent event) {
        String driverMatchesDestination = String.format("/topic/users/%d/matches", event.getDriverId());
        String passengerMatchesDestination = String.format("/topic/users/%d/matches", event.getPassengerId());

        log.info("event={} {}", event.getName(), event);
        if (log.isDebugEnabled()) {
            log.debug("broadcast-destination={}, {}", driverMatchesDestination, passengerMatchesDestination);
        }

        simpMessaging.convertAndSend(driverMatchesDestination, event);
        simpMessaging.convertAndSend(passengerMatchesDestination, event);
    }
}
