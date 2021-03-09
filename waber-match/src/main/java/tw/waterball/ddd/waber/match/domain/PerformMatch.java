package tw.waterball.ddd.waber.match.domain;

import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.user.Driver;

import javax.inject.Named;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

import static tw.waterball.ddd.commons.utils.StreamUtils.iterate;
import static tw.waterball.ddd.model.geo.Route.from;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
public class PerformMatch {
    private final DistanceCalculator distanceCalculator;

    public PerformMatch(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    public void execute(Match match, Collection<Driver> drivers) {
        execute(match, drivers.iterator());
    }

    public void execute(Match match, Iterator<Driver> drivers) {
        Location startLocation = match.getPreferences().getStartLocation();

        iterate(drivers)
                .min(minimumDistanceRule(startLocation))
                .ifPresent(match::matchDriver);
    }

    private Comparator<Driver> minimumDistanceRule(Location startLocation) {
        return (driver1, driver2) -> {
            double distance1 = distanceCalculator.calculate(from(startLocation).to(driver1.getLocation()));
            double distance2 = distanceCalculator.calculate(from(startLocation).to(driver2.getLocation()));
            return (int) (distance1 - distance2);
        };
    }
}
