package tw.waterball.ddd.waber.springboot.broker.brokers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tw.waterball.ddd.events.MatchCompleteEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Controller
@AllArgsConstructor
public class MatchBrokers {
    private SimpMessagingTemplate simpMessaging;

    @RabbitListener(queues = "/matches/complete")
    public void listenToMatch(MatchCompleteEvent event) {
        log.info("Event: {}.", event);
        String driverMatchesDestination = String.format("/app/drivers/%d/matches", event.getDriverId());
        String passengerMatchesDestination = String.format("/app/passengers/%d/matches", event.getPassengerId());
        simpMessaging.convertAndSend(driverMatchesDestination, event);
        simpMessaging.convertAndSend(passengerMatchesDestination, event);
    }
}
