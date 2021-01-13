package tw.waterball.ddd.waber.api.payment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class FakePaymentServiceDriver implements PaymentServiceDriver {
    private Map<String, PaymentView> payments = new HashMap<>();

    public FakePaymentServiceDriver() {
        this(Collections.emptySet());
    }

    public FakePaymentServiceDriver(Set<PaymentView> payments) {
        this.payments = payments.stream().collect(Collectors.toMap(p -> p.tripId, p -> p));
    }

    public void addPaymentView(PaymentView paymentView) {
        payments.put(paymentView.tripId, paymentView);
    }

    @Override
    public PaymentView createPayment(int passengerId, int matchId, String tripId) {
        return payments.get(tripId);
    }
}
