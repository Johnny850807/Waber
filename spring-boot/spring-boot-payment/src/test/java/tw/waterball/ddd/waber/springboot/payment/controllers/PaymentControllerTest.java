package tw.waterball.ddd.waber.springboot.payment.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.api.match.FakeMatchServiceDriver;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.trip.FakeTripServiceDriver;
import tw.waterball.ddd.api.trip.TripServiceDriver;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.payment.PricingItem;
import tw.waterball.ddd.model.payment.PricingStrategy;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.stubs.MatchStubs;
import tw.waterball.ddd.stubs.TripStubs;
import tw.waterball.ddd.stubs.UserStubs;
import tw.waterball.ddd.waber.api.payment.PaymentView;
import tw.waterball.ddd.waber.springboot.commons.profiles.FakeServiceDrivers;
import tw.waterball.ddd.waber.springboot.payment.PaymentApplication;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.api.match.MatchView.toViewModel;
import static tw.waterball.ddd.api.trip.TripView.toViewModel;

@ActiveProfiles(FakeServiceDrivers.NAME)
@ContextConfiguration(classes = {PaymentApplication.class, PaymentControllerTest.TestConfiguration.class})
class PaymentControllerTest extends AbstractSpringBootTest {
    private static PricingItem pricingItem = new PricingItem("Test", "Test", BigDecimal.valueOf(666));
    private Passenger passenger = UserStubs.NORMAL_PASSENGER;
    private Driver driver = UserStubs.NORMAL_DRIVER;
    private Match match = MatchStubs.COMPLETED_MATCH;
    private Trip trip = TripStubs.ARRIVED_TRIP;

    @Autowired
    private FakeMatchServiceDriver matchServiceDriver;

    @Autowired
    private FakeTripServiceDriver tripServiceDriver;

    @Configuration
    public static class TestConfiguration {
        @Bean
        @Primary
        PricingStrategy pricingStrategy() {
            return trip -> singletonList(pricingItem);
        }
    }

    @Test
    void GivenArrivedTrip_WhenCheckoutTheTripPayment_ThePaymentShouldBeCreatedCorrectly() throws Exception {
        matchServiceDriver.addMatchView(toViewModel(match));
        tripServiceDriver.addTripView(toViewModel(trip));

        PaymentView paymentView = getBody(
                mockMvc.perform(post("/api/passengers/{passengerId}/matches/{matchId}/trips/{tripId}/payment",
                        passenger.getId(), match.getId(), trip.getId()))
                        .andExpect(status().isOk()), PaymentView.class);

        assertEquals(pricingItem.getPrice().intValue(), paymentView.totalPrice);
    }

}