package tw.waterball.ddd.model.payment;

import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.trip.Trip;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@NoArgsConstructor
public class Payment {
    private One<Trip> trip = One.empty();
    private List<PricingItem> pricingItems;
    private BigDecimal totalPrice;
    private boolean isPaid;

    public Payment(String tripId, List<PricingItem> pricingItems, boolean isPaid) {
        trip.resolveId(tripId);
        this.pricingItems = pricingItems;
        this.totalPrice = calculateTotalPrice(pricingItems);
        this.isPaid = isPaid;
    }

    public void checkout(Trip trip, PricingStrategy pricingStrategy) {
        this.trip.resolveAssociation(trip);
        pricingItems = pricingStrategy.pricing(trip);
        totalPrice = calculateTotalPrice(pricingItems);
    }

    private BigDecimal calculateTotalPrice(List<PricingItem> pricingItems) {
        return pricingItems.stream()
                .map(PricingItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getId() {
        return getTripId();
    }

    public String getTripId() {
        return trip.getId();
    }

    public One<Trip> getTripAssociation() {
        return trip;
    }


    public List<PricingItem> getPricingItems() {
        return pricingItems;
    }

    public boolean isPaid() {
        return isPaid;
    }
}
