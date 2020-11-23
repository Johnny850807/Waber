package tw.waterball.ddd.waber.springboot.user.repositories;

import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.passenger.repositories.PassengerRepository;
import tw.waterball.ddd.waber.springboot.user.ports.UserPort;
import tw.waterball.ddd.waber.springboot.user.repositories.data.UserData;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Component
public class SpringBootPassengerRepository implements PassengerRepository {
    private UserPort userPort;

    public SpringBootPassengerRepository(UserPort userPort) {
        this.userPort = userPort;
    }

    @Override
    public Optional<Passenger> findById(int id) {
        Optional<UserData> data = userPort.findById(id);
        return data.map(UserData::toPassenger);
    }

    @Override
    public Passenger save(Passenger passenger) {
        UserData data = userPort.save(UserData.fromEntity(passenger));
        passenger.setId(data.id);
        return passenger;
    }
}
