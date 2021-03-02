package tw.waterball.ddd.waber.springboot.trip.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.waber.trip.usecases.StartTrip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class StartTripHandler {
    public static final String ROUTING_KEY = "/matches/complete";
    public static final String QUEUE_NAME = "waber-trip:StartTripHandler";
    private final StartTrip startTrip;
    private Runnable onHandledListener = () -> {};

    @Bean
    public Queue startTripHandlerQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding eventsBinding(@Qualifier("eventsExchange") DirectExchange exchange,
                                 @Qualifier("startTripHandlerQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange).with(ROUTING_KEY);
    }

    public void setOnHandledListener(Runnable onHandledListener) {
        this.onHandledListener = onHandledListener;
    }


    @RabbitListener(queues = QUEUE_NAME)
    public void startTrip(MatchCompleteEvent event) {
        log.info("Received Event: {}", event);
        startTrip.execute(new StartTrip.Request(event.getPassengerId(), event.getMatchId()),
                trip -> { /*Present Nothing*/ });

        onHandledListener.run();
    }
}
