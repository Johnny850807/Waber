package tw.waterball.ddd.waber.user.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
public class UpdateLatestLocation {
    private UserRepository userRepository;

    public UpdateLatestLocation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
