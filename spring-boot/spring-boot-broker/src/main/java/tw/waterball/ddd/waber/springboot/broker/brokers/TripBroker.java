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
import tw.waterball.ddd.events.TripStateChangedEvent;


/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class TripBroker {
    public static final String ROUTING_KEY = "/trips/state/change";
    public static final String QUEUE_NAME = "waber-broker:TripBroker";
    private final SimpMessagingTemplate simpMessaging;

    @Bean
    public Binding bindTripQueueToEvents(@Qualifier("eventsExchange") DirectExchange exchange,
                                 @Qualifier("tripQueue") Queue queue) {
        log.info("Exchange binding: {} -> {} (Key:{}).", queue.getName(), exchange.getName(), ROUTING_KEY);
        return BindingBuilder.bind(queue)
                .to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue tripQueue() {
        return new Queue(QUEUE_NAME);
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void listenToMatch(TripStateChangedEvent event) {
        String[] destinations = {String.format("/topic/trips/%s", event.getTripId()),
                String.format("/topic/users/%d/trips/current/state", event.getPassengerId())};

        for (String destination : destinations) {
            log.info("Event: {}, Broadcast to => {}", event, destination);
            simpMessaging.convertAndSend(destination, event.getState().toString());
        }
    }
}
