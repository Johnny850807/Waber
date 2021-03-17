package tw.waterball.ddd.waber.match.domain;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;
import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.user.Driver;

import javax.inject.Named;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
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

    @WithSpan
    public void execute(Match match, Collection<Driver> drivers) {
        currentSpan(attr("candidates", drivers.size()),
                attr("preferences", match.getPreferences()));


        Location startLocation = match.getPreferences().getStartLocation();

        drivers.stream()
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
