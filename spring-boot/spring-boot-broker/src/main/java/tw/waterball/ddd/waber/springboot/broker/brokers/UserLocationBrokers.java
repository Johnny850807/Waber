package tw.waterball.ddd.waber.springboot.broker.brokers;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@AllArgsConstructor
@Controller
public class UserLocationBrokers {
    private final SimpMessagingTemplate simpMessaging;

    @Bean
    public Queue userLocationQueue() {
        return new Queue("/users/location");
    }

    @RabbitListener(queues = "/users/location")
    public void listenToMatch(UserLocationUpdatedEvent event) {
        log.info("Event: {}.", event);
        String destination = String.format("/users/%d/location", event.getUserId());
        simpMessaging.convertAndSend(destination, event);
    }

}
