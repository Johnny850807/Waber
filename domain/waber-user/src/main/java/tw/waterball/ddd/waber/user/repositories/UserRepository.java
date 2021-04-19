package tw.waterball.ddd.waber.user.repositories;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface UserRepository {
    List<Driver> getAllAvailableDrivers();

    Optional<User> findById(int userId);

    List<User> findAllByIds(Iterable<Integer> userIds);

    List<Driver> findAllAvailableDriversByCarType(Driver.CarType carType);

    User save(User user);
    User saveUserWithHisPassword(User user, String password);

    Optional<User> findByEmailAndPassword(String email, String password);

    void updateLatestLocation(int userId, Location location);


    void removeAll();

}
