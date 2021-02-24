package tw.waterball.ddd.waber.springboot.broker.brokers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tw.waterball.ddd.events.MatchCompleteEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Controller
@Configuration
@AllArgsConstructor
public class MatchBroker {
    private final SimpMessagingTemplate simpMessaging;

    @Bean
    public Queue matchCompleteQueue() {
        return new Queue("/matches/complete");
    }

    @RabbitListener(queues = "/matches/complete")
    public void listenToMatch(MatchCompleteEvent event) {
        log.info("Event: {}.", event);
        String driverMatchesDestination = String.format("/topic/drivers/%d/matches", event.getDriverId());
        String passengerMatchesDestination = String.format("/topic/passengers/%d/matches", event.getPassengerId());
        simpMessaging.convertAndSend(driverMatchesDestination, event);
        simpMessaging.convertAndSend(passengerMatchesDestination, event);
    }
}
