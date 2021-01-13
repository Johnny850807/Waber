package tw.waterball.ddd.waber.api.payment;

import tw.waterball.ddd.model.payment.Price;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class PaymentView {
    public String tripId;
    public Price price;

    public PaymentView(String tripId, Price price) {
        this.tripId = tripId;
        this.price = price;
    }
}
