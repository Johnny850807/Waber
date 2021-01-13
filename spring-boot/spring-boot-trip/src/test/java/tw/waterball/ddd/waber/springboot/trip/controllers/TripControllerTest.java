package tw.waterball.ddd.waber.springboot.trip.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.api.match.FakeMatchServiceDriver;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.stubs.UserStubs;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;
import tw.waterball.ddd.waber.springboot.trip.TripApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.api.match.MatchView.toViewModel;

@ContextConfiguration(classes = {TripApplication.class, TripControllerTest.TestConfig.class})
@AutoConfigureMockMvc
class TripControllerTest extends AbstractSpringBootTest {
    Passenger passenger = UserStubs.NORMAL_PASSENGER;
    Driver driver = UserStubs.NORMAL_DRIVER;
    Match match;

    @Autowired
    FakeMatchServiceDriver matchServiceDriver;


    @Configuration
    public static class TestConfig {
        @Primary
        @Bean
        public FakeMatchServiceDriver fakeMatchServiceDriver() {
            return new FakeMatchServiceDriver();
        }
    }

    @Test
    void GivenMatch_WhenStartTrip_ShouldSucceed() throws Exception {
        givenMatch();
        startTrip();
    }

    private void givenMatch() {
        match = new Match(345, passenger.getId(), driver, new MatchPreferences());
        matchServiceDriver.addMatchView(toViewModel(match));
    }

    private void startTrip() throws Exception {
        mockMvc.perform(
                post("/api/users/{passengerId}/match/{matchId}/trip",
                        passenger.getId(), match.getId()))
                .andExpect(status().isOk());
    }
}