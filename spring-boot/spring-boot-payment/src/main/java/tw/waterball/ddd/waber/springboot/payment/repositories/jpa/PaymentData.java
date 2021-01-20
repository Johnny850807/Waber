package tw.waterball.ddd.waber.springboot.payment.repositories.jpa;

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
public class PaymentData {
    public String tripId;
    public List<PricingItem> pricingItems;
    public boolean isPaid;

    public static PaymentData toData(Payment payment) {
        return new PaymentData(payment.getTripId(), payment.getPricingItems(), payment.isPaid());
    }

    public Payment toEntity() {
        return new Payment(tripId, pricingItems, isPaid);
    }
}
