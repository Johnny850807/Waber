package tw.waterball.ddd.model.payment;

import lombok.NoArgsConstructor;
import lombok.ToString;
import tw.waterball.ddd.model.associations.One;
import tw.waterball.ddd.model.trip.Trip;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ToString
@NoArgsConstructor
public class Payment {
    private String tripId;
    private List<PricingItem> pricingItems;
    private BigDecimal totalPrice;
    private boolean isPaid;

    public Payment(String tripId, List<PricingItem> pricingItems) {
        this(tripId, pricingItems, false);
    }

    public Payment(String tripId, List<PricingItem> pricingItems, boolean isPaid) {
        this.tripId = tripId;
        this.pricingItems = pricingItems;
        this.totalPrice = calculateTotalPrice(pricingItems);
        this.isPaid = isPaid;
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
        return tripId;
    }

    public List<PricingItem> getPricingItems() {
        return pricingItems;
    }

    public boolean isPaid() {
        return isPaid;
    }
}
