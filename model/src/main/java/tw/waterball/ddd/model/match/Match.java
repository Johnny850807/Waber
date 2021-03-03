package tw.waterball.ddd.model.match;

import tw.waterball.ddd.model.associations.Many;
import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.associations.ZeroOrOne;
import tw.waterball.ddd.model.base.AggregateRoot;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;

import java.util.*;

import static tw.waterball.ddd.commons.utils.StreamUtils.iterate;
import static tw.waterball.ddd.model.geo.Route.from;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Match extends AggregateRoot<Integer> {
    private final MatchPreferences preferences;
    private final One<Passenger> passenger = new One<>();
    private final ZeroOrOne<Driver> driver = new ZeroOrOne<>();
    private boolean alive = true;
    private Date createdDate = new Date();

    public static Match start(Passenger passenger, MatchPreferences preferences) {
        return new Match(passenger, preferences);
    }

    public Match(Integer id, int passengerId, Integer driverId, MatchPreferences preferences, Date createdDate, boolean alive) {
        super(id);
        this.alive = alive;
        passenger.resolveId(passengerId);
        driver.resolveId(driverId);
        this.preferences = preferences;
        this.createdDate = createdDate;
    }

    public Match(Integer id, int passengerId, Driver driver, MatchPreferences preferences) {
        super(id);
        passenger.resolveId(passengerId);
        this.driver.resolveAssociation(driver);
        this.preferences = preferences;
    }

    private Match(Passenger passenger, MatchPreferences preferences) {
        this.passenger.resolveAssociation(passenger);
        this.preferences = preferences;
    }

    public void perform(Many<Driver> drivers, DistanceCalculator distanceCalculator) {
        perform(drivers.iterator(), distanceCalculator);
    }
    public void perform(Collection<Driver> drivers, DistanceCalculator distanceCalculator) {
        perform(drivers.iterator(), distanceCalculator);
    }

    public void perform(Iterator<Driver> drivers, DistanceCalculator distanceCalculator) {
        iterate(drivers)
                .min(minimumDistanceDriver(distanceCalculator))
                .ifPresent(this::complete);
    }

    private Comparator<Driver> minimumDistanceDriver(DistanceCalculator distanceCalculator) {
        return (driver1, driver2) -> {
            Location start = preferences.getStartLocation();
            double distance1 = distanceCalculator.calculate(from(start).to(driver1.getLocation()));
            double distance2 = distanceCalculator.calculate(from(start).to(driver2.getLocation()));
            return (int) (distance1 - distance2);
        };
    }

    public void cancel() {
        driver.reset();
    }

    public void complete(Driver driver) {
        this.driver.resolveAssociation(driver);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Driver getDriver() {
        return getDriverOptional().get();
    }

    public int getDriverId() {
        return (int) driver.getId().get();
    }

    public Optional<Driver> getDriverOptional() {
        return driver.get();
    }

    public MatchPreferences getPreferences() {
        return preferences;
    }

    public Passenger getPassenger() {
        return passenger.get();
    }

    public int getPassengerId() {
        return passenger.getId();
    }

    public One<Passenger> getPassengerAssociation() {
        return passenger;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
