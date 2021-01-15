package tw.waterball.ddd.model.payment;

import tw.waterball.ddd.model.trip.Trip;

import javax.inject.Named;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public List<PricingItem> pricing(Trip trip) {
        return null;
    }
}
