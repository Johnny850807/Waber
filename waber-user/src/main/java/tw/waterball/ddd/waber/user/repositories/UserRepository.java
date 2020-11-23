package tw.waterball.ddd.waber.user.repositories;

import tw.waterball.ddd.model.geo.Location;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface UserRepository {
    void updateLatestLocation(int userId, Location location);
}
