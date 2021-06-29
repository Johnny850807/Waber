package tw.waterball.ddd.waber.api.payment;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class FakeUserContext implements UserContext {
    public Map<Integer, Driver> drivers;
    private Map<Integer, Passenger> passengers;

    public FakeUserContext() {
        this(Collections.emptySet(), Collections.emptySet());
    }

    public FakeUserContext(Set<Driver> drivers, Set<Passenger> passengers) {
        this.drivers = drivers.stream().collect(Collectors.toMap(Driver::getId, d -> d));
        this.passengers = passengers.stream().collect(Collectors.toMap(Passenger::getId, p -> p));
    }

    public void addDriver(Driver driver) {
        drivers.put(driver.getId(), driver);
    }

    public void addPassenger(Passenger passenger) {
        passengers.put(passenger.getId(), passenger);
    }

    @Override
    public void uploadLocation(int userId, Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Driver signUpAsDriver(String name, String email, String password, Driver.CarType carType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Passenger signUpAsPassenger(String name, String email, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Driver getDriver(int driverId) {
        return drivers.get(driverId);
    }

    @Override
    public synchronized void changeDriverStatus(int driverId, Driver.Status status) throws DriverNotAvailableException {
        drivers.get(driverId).setStatus(status);
    }

    @Override
    public synchronized Passenger getPassenger(int passengerId) {
        return passengers.get(passengerId);
    }

    @Override
    public synchronized List<Driver> getDrivers(MatchPreferences matchPreferences) {
        return drivers.values().stream()
                .filter(d -> d.getCarType() == matchPreferences.getCarType())
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(int userId) {
        Driver driver = drivers.get(userId);
        if (driver == null) {
            return passengers.get(userId);
        }
        return driver;
    }

    public void reset() {
        drivers.clear();
        passengers.clear();
    }
}
