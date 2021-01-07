package tw.waterball.ddd.waber.user.usecases;

import lombok.AllArgsConstructor;
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
    private ActivityRepository activityRepository;
    private UserRepository userRepository;

    public void execute(Request req) {
        Activity activity = activityRepository.findByName(req.activityName)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found."));
        Driver driver = (Driver) userRepository.associateById(req.driverId);
        activity.participate(driver);
        activityRepository.save(activity);
    }

    @AllArgsConstructor
    public static class Request {
        public String activityName;
        public int driverId;
    }
}
