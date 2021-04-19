package tw.waterball.ddd.waber.passenger.repositories;

import tw.waterball.ddd.model.user.Activity;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface ActivityRepository {

    Optional<Activity> findByName(String name);

    Activity save(Activity activity);

    void removeAll();
}
