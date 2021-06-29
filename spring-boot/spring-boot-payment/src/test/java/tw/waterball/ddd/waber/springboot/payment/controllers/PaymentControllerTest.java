package tw.waterball.ddd.waber.springboot.payment.controllers;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.api.match.MatchView.toViewModel;
import static tw.waterball.ddd.api.trip.TripView.toViewModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.api.match.FakeMatchContext;
import tw.waterball.ddd.api.trip.FakeTripContext;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.payment.PricingItem;
import tw.waterball.ddd.model.payment.PricingStrategy;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.stubs.MatchStubs;
import tw.waterball.ddd.stubs.TripStubs;
import tw.waterball.ddd.waber.api.payment.PaymentView;
import tw.waterball.ddd.waber.pricing.repositories.PaymentRepository;
import tw.waterball.ddd.waber.springboot.commons.profiles.FakeServiceDrivers;
import tw.waterball.ddd.waber.springboot.payment.PaymentApplication;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;

import java.math.BigDecimal;

@AutoConfigureDataMongo
@ActiveProfiles(FakeServiceDrivers.NAME)
@ContextConfiguration(classes = {PaymentApplication.class, PaymentControllerTest.TestConfiguration.class})
class PaymentControllerTest extends AbstractSpringBootTest {
    private static final PricingItem pricingItem = new PricingItem("Test", "Test", BigDecimal.valueOf(666));
    private final Match match = MatchStubs.COMPLETED_MATCH;
    private final Trip trip = TripStubs.ARRIVED_TRIP;

    @Autowired
    private FakeMatchContext matchServiceDriver;

    @Autowired
    private FakeTripContext tripServiceDriver;

    @Autowired
    private PaymentRepository paymentRepository;

    @Configuration
    public static class TestConfiguration {
        @Bean
        @Primary
        PricingStrategy pricingStrategy() {
            return trip -> singletonList(pricingItem);
        }

    }

    @AfterEach
    void cleanUp() {
        paymentRepository.clearAll();
    }

    @Test
    void GivenArrivedTrip_WhenCheckoutTheTripPayment_ThePaymentShouldBeCreatedCorrectly() throws Exception {
        matchServiceDriver.addMatchView(toViewModel(match));
        tripServiceDriver.addTripView(toViewModel(trip));

        PaymentView paymentView = checkoutPayment();

        assertEquals(pricingItem.getPrice().intValue(), paymentView.totalPrice);
    }

    private PaymentView checkoutPayment() throws Exception {
        return getBody(
                mockMvc.perform(post("/api/payments/trips/{tripId}", trip.getId()))
                        .andExpect(status().isOk()), PaymentView.class);
    }

}