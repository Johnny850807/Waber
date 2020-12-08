package tw.waterball.ddd.waber.match.repositories;

import tw.waterball.ddd.model.match.Activity;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface ActivityRepository {

    Optional<Activity> findByName(String name);

    Activity save(Activity activity);
}
