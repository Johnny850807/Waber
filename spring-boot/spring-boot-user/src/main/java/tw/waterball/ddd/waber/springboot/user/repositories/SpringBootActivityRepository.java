package tw.waterball.ddd.waber.springboot.user.repositories;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.ActivityData;
import tw.waterball.ddd.waber.springboot.user.repositories.jpa.JpaActivityDataPort;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class SpringBootActivityRepository implements ActivityRepository {
    private JpaActivityDataPort dataPort;

    public SpringBootActivityRepository(JpaActivityDataPort dataPort) {
        this.dataPort = dataPort;
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return dataPort.findByName(name)
                .map(ActivityData::toEntity);
    }

    @Override
    public Activity save(Activity activity) {
        ActivityData data = dataPort.save(ActivityData.fromEntity(activity));
        activity.setId(data.getId());
        return activity;
    }

}
