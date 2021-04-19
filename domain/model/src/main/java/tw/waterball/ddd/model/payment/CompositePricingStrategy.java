package tw.waterball.ddd.model.payment;

import tw.waterball.ddd.model.trip.Trip;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Chain of Responsibility for Open-Close.
 * @author Waterball (johnny850807@gmail.com)
 */
public class CompositePricingStrategy implements PricingStrategy {
    private final List<PricingStrategy> pricingStrategies;

    public CompositePricingStrategy(List<PricingStrategy> pricingStrategies) {
        this.pricingStrategies = pricingStrategies;
    }

    @Override
    public List<PricingItem> pricing(Trip trip) {
        return pricingStrategies.stream()
                .filter(p -> matchTrip(trip, p))
                .flatMap(p -> p.pricing(trip).stream())
                .collect(Collectors.toList());
    }

    private boolean matchTrip(Trip trip, PricingStrategy p) {
        return !(p instanceof PricingFilter) || ((PricingFilter) p).match(trip);
    }

    public interface PricingFilter extends PricingStrategy {
        boolean match(Trip trip);
    }
}
