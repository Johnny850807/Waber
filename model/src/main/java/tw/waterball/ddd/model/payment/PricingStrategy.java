package tw.waterball.ddd.model.payment;

import tw.waterball.ddd.model.trip.Trip;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface PricingStrategy {
    List<PricingItem> pricing(Trip trip);
}
