package tw.waterball.ddd.events;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class EventBus {
    private Collection<Subscriber> subscribers = new HashSet<>();

    public EventBus() {
        this(Collections.emptySet());
    }

    public EventBus(Collection<Subscriber> subscribers) {
        subscribers.forEach(this::subscribe);
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(Event event) {
        subscribers.forEach(subscriber -> subscriber.onEvent(event));
    }

    public interface Subscriber {
        void onEvent(Event event);
    }
}
