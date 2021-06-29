package tw.waterball.ddd.waber.api.payment;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;

import static java.util.Objects.requireNonNull;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestPaymentContext implements PaymentContext {
    private final BaseUrl paymentServiceBaseUrl;
    private final RestTemplate api;

    @Override
    public PaymentView checkoutPayment(String tripId) {
        return api.postForEntity(
                paymentServiceBaseUrl.withPath("/api/payments/trips/{tripId}"),
                null,
                PaymentView.class, tripId).getBody();
    }
}