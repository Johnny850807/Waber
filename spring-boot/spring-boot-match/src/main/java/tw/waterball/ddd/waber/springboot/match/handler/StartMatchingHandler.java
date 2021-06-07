package tw.waterball.ddd.waber.springboot.match.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
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
public class StartMatchingHandler {
    public static final String QUEUE_NAME = "waber-trip:StartMatchingHandler";
    private final MatchUseCase matchUseCase;

    @Bean
    public Queue startTripHandlerQueue() {
        return new Queue(QUEUE_NAME);
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void handleStartMatching(@Header(name = "traceparent", required = false) String traceparent,
                                    StartMatchingCommand command) {
        matchUseCase.execute(new MatchRequest(command.getMatchId()));
    }
}
