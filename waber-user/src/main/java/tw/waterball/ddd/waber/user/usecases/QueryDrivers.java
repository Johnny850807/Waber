package tw.waterball.ddd.waber.user.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
@AllArgsConstructor
public class QueryDrivers {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public Collection<Driver> execute(Request req) {
        if (req.activityName != null && !req.activityName.trim().isEmpty()) {
            Activity activity = activityRepository.findByName(req.activityName)
                    .orElseThrow(NotFoundException::new);
            return activity.getParticipantDrivers().stream()
                    .filter(driver -> driver.getStatus() == Driver.Status.AVAILABLE)
                    .filter(driver -> driver.getCarType() == req.carType)
                    .collect(Collectors.toList());
        } else if (req.carType != null) {
            return userRepository.findAllAvailableDriversByCarType(req.carType);
        }
        return userRepository.findAllAvailableDrivers();
    }

    @AllArgsConstructor
    public static class Request {
        public String activityName;
        public Driver.CarType carType;
    }
}
