package tw.waterball.ddd.waber.match.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.model.match.Activity;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.match.repositories.ActivityRepository;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Named
public class ParticipateActivity {
    private ActivityRepository activityRepository;

    public ParticipateActivity(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void execute(Request req) {
        Activity activity = activityRepository.findByName(req.activityName)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found."));
        activity.participate(req.driver);
        activityRepository.save(activity);
    }

    @AllArgsConstructor
    public static class Request {
        public String activityName;
        public Driver driver;
    }
}
