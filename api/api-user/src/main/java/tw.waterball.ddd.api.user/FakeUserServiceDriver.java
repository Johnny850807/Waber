package tw.waterball.ddd.api.user;

import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverHasBeenMatchedException;
import tw.waterball.ddd.model.user.Passenger;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class FakeUserServiceDriver implements UserServiceDriver {
    public Map<Integer, Driver> drivers;
    private Map<Integer, Passenger> passengers;

    public FakeUserServiceDriver() {
        this(Collections.emptySet(), Collections.emptySet());
    }

    public FakeUserServiceDriver(Set<Driver> drivers, Set<Passenger> passengers) {
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
    public synchronized Driver getDriver(int driverId) {
        return drivers.get(driverId);
    }

    @Override
    public synchronized void setDriverStatus(int driverId, Driver.Status status) throws DriverHasBeenMatchedException {
        drivers.get(driverId).setStatus(status);
    }

    @Override
    public synchronized Passenger getPassenger(int passengerId) {
        return passengers.get(passengerId);
    }

    @Override
    public synchronized List<Driver> filterDrivers(MatchPreferences matchPreferences) {
        return drivers.values().stream()
                .filter(d -> d.getCarType() == matchPreferences.getCarType())
                .collect(Collectors.toList());
    }
}
