package tw.waterball.ddd.model.payment;

import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.trip.Trip;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class Payment {
    private One<Trip> trip = One.empty();
    private PricingStrategy pricingStrategy;
    private List<PricingItem> pricingItems;
    private BigDecimal totalPrice;

    public Payment(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void checkout(Trip trip) {
        this.trip.resolveAssociation(trip);
        pricingItems = pricingStrategy.pricing(trip);
        totalPrice = pricingItems.stream()
                .map(PricingItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getTripId() {
        return trip.getId();
    }

    public One<Trip> getTripAssociation() {
        return trip;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public List<PricingItem> getPricingItems() {
        return pricingItems;
    }
}
