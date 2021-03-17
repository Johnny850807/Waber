package tw.waterball.ddd.waber.driver.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
@AllArgsConstructor
public class ParticipateActivity {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    @WithSpan
    public void execute(Request req) {
        Activity activity = activityRepository.findByName(req.activityName).orElseThrow(NotFoundException::new);
        Driver driver = (Driver) userRepository.findById(req.driverId).orElseThrow(NotFoundException::new);
        activity.participate(driver);
        activityRepository.save(activity);
    }

    @AllArgsConstructor
    public static class Request {
        public String activityName;
        public int driverId;
    }
}
