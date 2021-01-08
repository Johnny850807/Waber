package tw.waterball.ddd.api.user;

import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface UserServiceDriver {
    Driver getDriver(int driverId);

    Passenger getPassenger(int passengerId);

    List<Driver> filterDrivers(MatchPreferences matchPreferences);
}
