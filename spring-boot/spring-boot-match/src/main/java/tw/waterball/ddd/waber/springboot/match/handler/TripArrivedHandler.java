package tw.waterball.ddd.waber.springboot.match.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.events.TripStateChangedEvent;
import tw.waterball.ddd.model.trip.TripStateType;
import tw.waterball.ddd.waber.match.usecases.FinalizeMatch;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Component
@AllArgsConstructor
public class TripArrivedHandler {
    public static final String QUEUE_NAME = "waber-trip:TripArrivedHandler";
    public static final String ROUTING_KEY = "/trips/state/change";
    private final FinalizeMatch finalizeMatch;

    @Bean
    public Queue tripArrivedHandlerQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding bindTripArrivedHandlerQueueToEventsExchange(
            @Qualifier("eventsExchange") DirectExchange exchange,
            @Qualifier("tripArrivedHandlerQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange).with(ROUTING_KEY);
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void handleTripArrived(TripStateChangedEvent event) {
        if (event.getState() == TripStateType.ARRIVED) {
            log.info("[Finalize Match]");
            finalizeMatch.execute(event.getMatchId());
        }
    }
}
