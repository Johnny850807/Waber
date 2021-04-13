package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class SpringBootActivityRepository implements ActivityRepository {
    private ActivityDAO activityDAO;

    public SpringBootActivityRepository(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return activityDAO.findByName(name)
                .map(ActivityData::toEntity);
    }

    @Override
    public Activity save(Activity activity) {
        ActivityData data = activityDAO.save(ActivityData.fromEntity(activity));
        activity.setId(data.getId());
        return activity;
    }

    @Override
    public void clearAll() {
        activityDAO.deleteAll();
    }

}
