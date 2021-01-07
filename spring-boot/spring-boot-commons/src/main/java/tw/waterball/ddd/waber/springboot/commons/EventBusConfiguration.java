package tw.waterball.ddd.waber.springboot.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.events.EventBus;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Configuration
public class EventBusConfiguration {

    @Bean
    public EventBus eventBus(List<EventBus.Subscriber> subscribers) {
        return new EventBus(subscribers);
    }
}
