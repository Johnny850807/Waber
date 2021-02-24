package tw.waterball.ddd.waber.springboot.broker.brokers;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Component
public class HealthCheckBroker {
    private final SimpMessagingTemplate simpMessaging;

    public HealthCheckBroker(SimpMessagingTemplate simpMessaging) {
        this.simpMessaging = simpMessaging;
    }

    @Scheduled(fixedRate = 5000)
    public void healthBroadcast() {
        simpMessaging.convertAndSend("/topic/health", "OK");
    }
}
