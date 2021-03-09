package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

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

import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
@AllArgsConstructor
public class SpringBootUserRepository implements UserRepository {
    private final JpaUserDataPort jpaUserDataPort;
    private final JpaPasswordPort jpaPasswordPort;

    @Override
    public List<Driver> getAllAvailableDrivers() {
        return jpaUserDataPort.findAllByDriverIsTrueAndDriverStatusIs(Driver.Status.AVAILABLE.toString())
                .stream().map(UserData::toDriver)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(int userId) {
        return jpaUserDataPort.findById(userId)
                .map(UserData::toEntity);
    }

    @Override
    public User save(User user) {
        UserData data = jpaUserDataPort.save(toData(user));
        user.setId(data.getId());
        return user;
    }

    @Override
    @Transactional
    public User saveUserWithHisPassword(User user, String password) {
        UserData data = jpaUserDataPort.save(toData(user));
        jpaPasswordPort.save(new Password(data.getId(), password));
        user.setId(data.getId());
        return user;
    }

    @Override
    public List<User> findAllByIds(Iterable<Integer> userIds) {
        return jpaUserDataPort.findAllById(userIds)
                .stream().map(UserData::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> findAllAvailableDriversByCarType(Driver.CarType carType) {
        return jpaUserDataPort.findAllByCarTypeAndDriverStatusIs(carType, Driver.Status.AVAILABLE.toString())
                .stream().map(UserData::toDriver)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        var user = jpaUserDataPort.findByEmail(email)
                .map(UserData::toEntity);
        if (user.isPresent()) {
            var p = jpaPasswordPort.findById(user.get().getId());
            if (p.isPresent()) {
                return user;
            }
        }
        return Optional.empty();
    }

    @Override
    public void updateLatestLocation(int userId, Location location) {
        UserData data = jpaUserDataPort.getOne(userId);
        data.setLatitude(location.getLatitude());
        data.setLongitude(location.getLongitude());
        jpaUserDataPort.save(data);
    }

    @Override
    public void clearAll() {
        jpaUserDataPort.deleteAll();
    }
}
