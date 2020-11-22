package tw.waterball.ddd.model.match;

import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.model.AggregateRoot;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.geo.Route;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.Driver;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

import static tw.waterball.ddd.commons.utils.StreamUtils.iterate;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Match extends AggregateRoot {
    private Passenger passenger;
    private MatchPreferences preferences;
    private Driver driver;

    public static Match start(Passenger passenger, MatchPreferences preferences) {
        return new Match(passenger, preferences);
    }

    private Match(Passenger passenger, MatchPreferences preferences) {
        this.passenger = passenger;
        this.preferences = preferences;
    }

    @Override
    public boolean isConsistent() {
        return preferences.isMatch(driver);
    }

    public Optional<Driver> match(Iterator<Driver> drivers, DistanceCalculator distanceCalculator) {
        return iterate(drivers)
                .filter(preferences::isMatch)
                .min(minimumDistanceDriver(distanceCalculator));
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
        // Do nothing
    }

    public MatchCompleteEvent complete(Driver driver) {
        this.driver = driver;
        return new MatchCompleteEvent(passenger.getId(), driver);
    }
}
