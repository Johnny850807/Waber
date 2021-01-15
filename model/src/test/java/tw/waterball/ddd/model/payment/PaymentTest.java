package tw.waterball.ddd.model.payment;

import org.junit.jupiter.api.Assertions;
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
    private Trip whateverTrip = PICKING_TRIP;
    private Payment payment;

    @Test
    void WhenCheckout_ShouldHaveCorrectTotalPrice() {
        givenPricing(4000000000d, 6000000000d);

        payment.checkout(whateverTrip);

        assertEquals(BigDecimal.valueOf(10000000000d)
                .doubleValue(), payment.getTotalPrice().doubleValue());
    }

    private void givenPricing(double... prices) {
        var mockPricingStrategy = mock(PricingStrategy.class);
        when(mockPricingStrategy.pricing(any())).thenReturn(
                Arrays.stream(prices)
                        .mapToObj(BigDecimal::valueOf)
                        .map(price -> new PricingItem("Mock", "Mock", price))
                        .collect(Collectors.toList()));
        payment = new Payment(mockPricingStrategy);
    }

}