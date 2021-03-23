package tw.waterball.ddd.waber.api.payment;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverIsNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface UserServiceDriver {

    void uploadLocation(int userId, Location location);

    Driver signUpAsDriver(String name, String email, String password, Driver.CarType carType);

    Passenger signUpAsPassenger(String name, String email, String password);

    Driver getDriver(int driverId);

    void setDriverStatus(int driverId, Driver.Status status) throws DriverIsNotAvailableException;

    Passenger getPassenger(int passengerId);

    List<Driver> filterDrivers(MatchPreferences matchPreferences);

    User getUser(int userId);
}
