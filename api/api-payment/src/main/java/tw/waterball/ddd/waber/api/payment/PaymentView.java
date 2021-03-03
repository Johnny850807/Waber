package tw.waterball.ddd.waber.api.payment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.payment.Payment;
import tw.waterball.ddd.model.payment.PricingItem;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@NoArgsConstructor
@AllArgsConstructor
public class PaymentView {
    public String tripId;
    public List<PricingItem> pricingItems;
    public int totalPrice;

    public static PaymentView toViewModel(Payment payment) {
        return new PaymentView(payment.getTripId(),
                payment.getPricingItems(),
                payment.getTotalPrice().intValue());
    }
}
