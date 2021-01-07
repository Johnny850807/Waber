package tw.waterball.ddd.model.match;

import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.associations.ZeroOrOne;
import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.geo.Route;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

import static tw.waterball.ddd.commons.utils.StreamUtils.iterate;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Match extends AggregateRoot<Integer> {
    private MatchPreferences preferences;
    private One<Passenger> passenger = new One<>();
    private ZeroOrOne<Driver> driver = new ZeroOrOne<>();

    public static Match start(Passenger passenger, MatchPreferences preferences) {
        return new Match(passenger, preferences);
    }

    public Match(Integer id, int passengerId, Integer driverId, MatchPreferences preferences) {
        super(id);
        passenger.resolveId(passengerId);
        driver.resolveId(driverId);
        this.preferences = preferences;
    }

    private Match(Passenger passenger, MatchPreferences preferences) {
        this.passenger.resolveAssociation(passenger);
        this.preferences = preferences;
    }

    public void perform(Iterator<Driver> drivers, DistanceCalculator distanceCalculator) {
        iterate(drivers)
                .min(minimumDistanceDriver(distanceCalculator))
                .ifPresent(this::complete);
    }

    private Comparator<Driver> minimumDistanceDriver(DistanceCalculator distanceCalculator) {
        return (driver1, driver2) -> {
            Location start = preferences.getStartLocation();
            double distance1 = distanceCalculator.calculate(Route.from(start).to(driver1.getLatestLocation()));
            double distance2 = distanceCalculator.calculate(Route.from(start).to(driver2.getLatestLocation()));
            return (int) (distance1 - distance2);
        };
    }

    public void cancel() {
        driver.reset();
    }

    public MatchCompleteEvent complete(Driver driver) {
        this.driver.resolveAssociation(driver);
        return new MatchCompleteEvent(passenger.get().getId(), driver);
    }

    public void setDriver(Driver driver) {
        this.driver.resolveAssociation(driver);
    }

    public boolean isPending() {
        return !driver.exists();
    }

    public boolean isCompleted() {
        return driver.exists();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public ZeroOrOne<Driver> getDriverAssociation() {
        return driver;
    }

    public Optional<Driver> getDriver() {
        return driver.get();
    }

    public MatchPreferences getPreferences() {
        return preferences;
    }

    public Passenger getPassenger() {
        return passenger.get();
    }
}
