package tw.waterball.ddd.waber.springboot.user.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Configuration
public class AmqpQueuesConfiguration {

    public static final String USERS_LOCATION = "/users/location";

    @Bean
    public Queue userLocationQueue() {
        return new Queue(USERS_LOCATION);
    }
}
