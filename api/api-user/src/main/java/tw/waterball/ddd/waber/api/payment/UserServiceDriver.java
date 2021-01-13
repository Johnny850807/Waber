package tw.waterball.ddd.waber.api.payment;

import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverHasBeenMatchedException;
import tw.waterball.ddd.model.user.Passenger;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface UserServiceDriver {
    Driver getDriver(int driverId);

    void setDriverStatus(int driverId, Driver.Status status) throws DriverHasBeenMatchedException;

    Passenger getPassenger(int passengerId);

    List<Driver> filterDrivers(MatchPreferences matchPreferences);
}
