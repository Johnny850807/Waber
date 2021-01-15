package tw.waterball.ddd.waber.springboot.payment.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.api.match.FakeMatchServiceDriver;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.trip.TripServiceDriver;
import tw.waterball.ddd.waber.springboot.payment.PaymentApplication;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {PaymentApplication.class, PaymentControllerTest.TestConfig.class})
class PaymentControllerTest extends AbstractSpringBootTest {
    @Autowired
    private MatchServiceDriver matchServiceDriver;

    @Autowired
    private TripServiceDriver tripServiceDriver;

    @Configuration
    public static class TestConfig {
        @Bean
        FakeMatchServiceDriver fakeMatchServiceDriver() {
            return new FakeMatchServiceDriver();
        }
    }

    @Test
    void GivenTrip_WhenCheckoutTheTripPayment_ThePaymentShouldBeCreatedCorrectly() {
        mockMvc.perform()
    }

}