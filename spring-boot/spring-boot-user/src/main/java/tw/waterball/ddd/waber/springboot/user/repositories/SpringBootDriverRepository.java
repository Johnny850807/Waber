package tw.waterball.ddd.waber.springboot.user.repositories;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.driver.repositories.DriverRepository;
import tw.waterball.ddd.waber.springboot.user.ports.UserPort;
import tw.waterball.ddd.waber.springboot.user.repositories.data.UserData;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class SpringBootDriverRepository implements DriverRepository {
    private UserPort userPort;

    public SpringBootDriverRepository(UserPort userPort) {
        this.userPort = userPort;
    }

    @Override
    public Driver save(Driver driver) {
        UserData data = userPort.save(UserData.fromEntity(driver));
        driver.setId(data.id);
        return driver;
    }
}
