package tw.waterball.ddd.waber.springboot.broker.brokers;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.context.Context;
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
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;


/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class UserLocationBroker {
    public static final String ROUTING_KEY = "/users/location";
    public static final String QUEUE_NAME = "waber-broker:UserLocationBroker";
    private final SimpMessagingTemplate simpMessaging;

    @Bean
    public Binding bindUserLocationQueueToEvents(@Qualifier("eventsExchange") DirectExchange exchange,
                                 @Qualifier("userLocationQueue") Queue queue) {
        log.info("Exchange binding: {} -> {} (Key:{}).", queue.getName(), exchange.getName(), ROUTING_KEY);
        return BindingBuilder.bind(queue)
                .to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue userLocationQueue() {
        return new Queue(QUEUE_NAME);
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void listenToMatch(@Header(name = "traceparent", required = false) String traceparent,
                              UserLocationUpdatedEvent event) {
        log.info("Traceparent: {}", traceparent);
        String destination = String.format("/topic/users/%d/location", event.getUserId());
        log.info("Event: {}, Broadcast to => {}", event, destination);
        simpMessaging.convertAndSend(destination, event);
    }

}
