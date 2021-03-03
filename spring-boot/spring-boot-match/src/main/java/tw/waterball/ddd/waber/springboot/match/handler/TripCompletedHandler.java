package tw.waterball.ddd.waber.springboot.match.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.events.StartMatchingCommand;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase.MatchRequest;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Component
@AllArgsConstructor
public class TripCompletedHandler {
    public static final String QUEUE_NAME = "waber-trip:TripCompletedHandler";
    private final MatchUseCase matchUseCase;

    @Bean
    public Queue startTripHandlerQueue() {
        return new Queue(QUEUE_NAME);
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void handleStartMatching(StartMatchingCommand command) {
        log.info("Start Matching: {}", command);
        matchUseCase.execute(new MatchRequest(command.getMatchId()));
    }
}
