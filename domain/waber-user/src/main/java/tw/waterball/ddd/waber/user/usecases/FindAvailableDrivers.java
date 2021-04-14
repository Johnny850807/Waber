package tw.waterball.ddd.waber.user.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
@AllArgsConstructor
public class FindAvailableDrivers {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    @WithSpan
    public Collection<Driver> execute(Request req) {
        if (req.hasActivityName) {
            return filterDriversFromActivityParticipants(req);
        } else if (req.hasCarType) {
            return userRepository.findAllAvailableDriversByCarType(req.carType);
        }
        return userRepository.getAllAvailableDrivers();
    }

    private List<Driver> filterDriversFromActivityParticipants(Request req) {
        Activity activity = activityRepository.findByName(req.activityName)
                .orElseThrow(NotFoundException::new);
        return activity.getParticipantDrivers().stream()
                .filter(driver -> driver.getStatus() == Driver.Status.AVAILABLE)
                .filter(driver -> driver.getCarType() == req.carType)
                .collect(Collectors.toList());
    }

    public static class Request {
        public String activityName;
        public Driver.CarType carType;
        public boolean hasActivityName;
        public boolean hasCarType;

        public Request(String activityName, Driver.CarType carType) {
            this.activityName = activityName == null ? null : activityName.trim();
            this.carType = carType;
            hasCarType = carType != null;
            hasActivityName = activityName != null && !activityName.isEmpty();
        }

    }
}
