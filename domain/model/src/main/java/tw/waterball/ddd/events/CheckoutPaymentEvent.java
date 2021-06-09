package tw.waterball.ddd.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.payment.Payment;
import tw.waterball.ddd.model.payment.PricingItem;
import tw.waterball.ddd.model.payment.PricingStrategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class CheckoutPaymentEvent extends Event {
    private String pricingStrategy;
    private String tripId;
    private List<PricingItem> pricingItems;
    private BigDecimal totalPrice;

    public CheckoutPaymentEvent(PricingStrategy pricingStrategy, Payment payment) {
        super("CheckoutPaymentEvent");
        this.pricingStrategy = pricingStrategy.getClass().getSimpleName();
        this.tripId = payment.getTripId();
        this.pricingItems = payment.getPricingItems();
        this.totalPrice = payment.getTotalPrice();
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("pricingStrategy", pricingStrategy);
        map.put("tripId", tripId);
        map.put("totalPrice", totalPrice.toPlainString());
        for (int i = 0; i < pricingItems.size(); i++) {
            var pricingItem = pricingItems.get(i);
            map.put("pricing-item-"+i+"-name", pricingItem.getName());
            map.put("pricing-item-"+i+"-price", String.valueOf(pricingItem.getPrice()));
        }
        return map;
    }
}
