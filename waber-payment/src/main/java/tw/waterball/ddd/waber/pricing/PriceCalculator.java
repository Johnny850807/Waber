package tw.waterball.ddd.waber.pricing;

import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.payment.Price;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface PriceCalculator {
    Price calculate(TripView trip);
}
