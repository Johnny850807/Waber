package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
@AllArgsConstructor
public class SpringBootUserRepository implements UserRepository {
    private final UserDAO userDAO;
    private final PasswordDAO passwordDAO;

    @Override
    public List<Driver> getAllAvailableDrivers() {
        return userDAO.findAllByDriverIsTrueAndDriverStatusIs(Driver.Status.AVAILABLE.toString())
                .stream().map(UserData::toDriver)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(int userId) {
        return userDAO.findById(userId)
                .map(UserData::toEntity);
    }

    @Override
    public User save(User user) {
        UserData data = userDAO.save(toData(user));
        user.setId(data.getId());
        return user;
    }

    @Override
    @Transactional
    public User saveUserWithHisPassword(User user, String password) {
        UserData data = userDAO.save(toData(user));
        passwordDAO.save(new Password(data.getId(), password));
        user.setId(data.getId());
        return user;
    }

    @Override
    public List<User> findAllByIds(Iterable<Integer> userIds) {
        return userDAO.findAllById(userIds)
                .stream().map(UserData::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findAllAvailableDriversByCarType(Driver.CarType carType) {
        return userDAO.findAllByCarTypeAndDriverStatusIs(carType, Driver.Status.AVAILABLE.toString())
                .stream().map(UserData::toDriver)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        var user = userDAO.findByEmail(email)
                .map(UserData::toEntity);
        if (user.isPresent()) {
            var p = passwordDAO.findById(user.get().getId());
            if (p.isPresent()) {
                return user;
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void updateLatestLocation(int userId, Location location) {
        UserData data = userDAO.getOne(userId);
        data.setLatitude(location.getLatitude());
        data.setLongitude(location.getLongitude());
        userDAO.save(data);
    }

    @Override
    public void removeAll() {
        userDAO.deleteAll();
    }
}
