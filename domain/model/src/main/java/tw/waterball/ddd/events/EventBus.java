package tw.waterball.ddd.events;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.event;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class EventBus {
    private final Collection<Subscriber> subscribers = new HashSet<>();

    public EventBus() {
        this(Collections.emptySet());
    }

    public EventBus(Collection<Subscriber> subscribers) {
        subscribers.forEach(this::subscribe);
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @WithSpan
    public void publish(Event event) {
        currentSpan(event(event.getName()));

        subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }

    public interface Subscriber {
        void onEvent(Event event);
    }
}
