package tw.waterball.ddd.waber.user.repositories;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface UserRepository {
    Optional<User> findById(int userId);

    List<User> findAllByIds(Iterable<Integer> userIds);

    User save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    void updateLatestLocation(int userId, Location location);

    User associateById(int id);

    void clearAll();

}
