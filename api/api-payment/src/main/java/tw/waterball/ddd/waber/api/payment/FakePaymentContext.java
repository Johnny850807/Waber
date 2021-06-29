package tw.waterball.ddd.waber.api.payment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class FakePaymentContext implements PaymentContext {
    private Map<String, PaymentView> payments = new HashMap<>();

    public FakePaymentContext() {
        this(Collections.emptySet());
    }

    public FakePaymentContext(Set<PaymentView> payments) {
        this.payments = payments.stream().collect(Collectors.toMap(p -> p.tripId, p -> p));
    }

    public void addPaymentView(PaymentView paymentView) {
        payments.put(paymentView.tripId, paymentView);
    }

    @Override
    public PaymentView checkoutPayment(String tripId) {
        return payments.get(tripId);
    }
}
