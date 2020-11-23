package tw.waterball.ddd.waber.springboot.user.repositories;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.springboot.user.ports.UserPort;
import tw.waterball.ddd.waber.springboot.user.repositories.data.UserData;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class SpringBootUserRepository implements UserRepository {
    private UserPort userPort;

    public SpringBootUserRepository(UserPort userPort) {
        this.userPort = userPort;
    }

    @Override
    public void updateLatestLocation(int userId, Location location) {
        UserData data = userPort.getOne(userId);
        data.latitude = location.getLatitude();
        data.longitude = location.getLongitude();
        userPort.save(data);
    }
}
