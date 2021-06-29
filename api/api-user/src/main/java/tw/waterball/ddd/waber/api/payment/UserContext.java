package tw.waterball.ddd.waber.api.payment;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface UserContext {

    void uploadLocation(int userId, Location location);

    Driver signUpAsDriver(String name, String email, String password, Driver.CarType carType);

    Passenger signUpAsPassenger(String name, String email, String password);

    Driver getDriver(int driverId);

    default void askDriverToMatch(int driverId) throws DriverNotAvailableException {
        changeDriverStatus(driverId, Driver.Status.MATCHED);
    }

    void changeDriverStatus(int driverId, Driver.Status status) throws DriverNotAvailableException;

    Passenger getPassenger(int passengerId);

    List<Driver> getDrivers(MatchPreferences matchPreferences);

    User getUser(int userId);
}
