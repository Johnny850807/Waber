package tw.waterball.ddd.waber.user.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
@AllArgsConstructor
public class UpdateLatestLocation {
    private final UserRepository userRepository;
    private final EventBus eventBus;

    @WithSpan
    public void execute(Request req) {
        userRepository.updateLatestLocation(req.userId, req.location);
        eventBus.publish(new UserLocationUpdatedEvent(req.userId, req.location));
    }

    @AllArgsConstructor
    public static class Request {
        public int userId;
        public Location location;
    }
}
