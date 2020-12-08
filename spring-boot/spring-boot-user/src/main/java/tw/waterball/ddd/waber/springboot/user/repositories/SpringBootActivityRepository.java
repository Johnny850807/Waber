package tw.waterball.ddd.waber.springboot.match.repositories;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.match.Activity;
import tw.waterball.ddd.waber.match.repositories.ActivityRepository;
import tw.waterball.ddd.waber.springboot.match.repositories.jpa.ActivityData;
import tw.waterball.ddd.waber.springboot.match.repositories.jpa.JpaActivityDataPort;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class SpringBootActivityRepository implements ActivityRepository {
    private JpaActivityDataPort jpaActivityDataPort;

    public SpringBootActivityRepository(JpaActivityDataPort jpaActivityDataPort) {
        this.jpaActivityDataPort = jpaActivityDataPort;
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return jpaActivityDataPort.findByName(name)
                .map(ActivityData::toEntity);
    }

    @Override
    public Activity save(Activity activity) {
        ActivityData data = jpaActivityDataPort.save(ActivityData.fromEntity(activity));
        activity.setId(data.id);
        return activity;
    }
}
