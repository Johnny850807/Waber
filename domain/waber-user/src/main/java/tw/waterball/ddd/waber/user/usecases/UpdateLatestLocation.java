package tw.waterball.ddd.waber.user.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
public class UpdateLatestLocation {
    private final UserRepository userRepository;

    public UpdateLatestLocation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @WithSpan
    public void execute(Request req, EventBus eventBus) {
        userRepository.updateLatestLocation(req.userId, req.location);
        eventBus.publish(new UserLocationUpdatedEvent(req.userId, req.location));
    }

    @AllArgsConstructor
    public static class Request {
        public int userId;
        public Location location;
    }
}
