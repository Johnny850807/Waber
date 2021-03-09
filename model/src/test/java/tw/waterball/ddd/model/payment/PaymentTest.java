package tw.waterball.ddd.model.payment;

import org.junit.jupiter.api.Test;
import tw.waterball.ddd.model.trip.Trip;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tw.waterball.ddd.stubs.TripStubs.PICKING_TRIP;

class PaymentTest {
    private final Trip whateverTrip = PICKING_TRIP;
    private PricingStrategy pricingStrategy;

    @Test
    void WhenCheckout_ShouldHaveCorrectTotalPrice() {
        givenPricing(4000000000d, 6000000000d);

        Payment payment = payment();

        assertEquals(BigDecimal.valueOf(10000000000d)
                .doubleValue(), payment.getTotalPrice().doubleValue());
    }

    private void givenPricing(double... prices) {
        pricingStrategy = mock(PricingStrategy.class);
        when(pricingStrategy.pricing(any())).thenReturn(
                Arrays.stream(prices)
                        .mapToObj(BigDecimal::valueOf)
                        .map(price -> new PricingItem("Mock", "Mock", price))
                        .collect(Collectors.toList()));
    }

    private Payment payment() {
        return new Payment(whateverTrip.getId(), pricingStrategy.pricing(whateverTrip));
    }

}