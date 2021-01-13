package tw.waterball.ddd.waber.springboot.match.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.user.FakeUserServiceDriver;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.stubs.UserStubs;
import tw.waterball.ddd.waber.springboot.match.MatchApplication;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.commons.utils.SneakyUtils.sneakyThrows;

@ContextConfiguration(classes = {MatchApplication.class, MatchControllerTest.TestConfig.class})
public class MatchControllerTest extends AbstractSpringBootTest {
    private Driver driver = UserStubs.NORMAL_DRIVER;
    private Passenger passenger = UserStubs.NORMAL_PASSENGER;
    private MatchPreferences preferences = new MatchPreferences(
            passenger.getLatestLocation(), driver.getCarType(), null);

    @Autowired
    FakeUserServiceDriver userServiceDriver;

    @Configuration
    public static class TestConfig {
        @Primary
        @Bean
        public FakeUserServiceDriver fakeUserServiceDriver() {
            return new FakeUserServiceDriver();
        }
    }

    @BeforeEach
    public void setup() {
        userServiceDriver.addPassenger(passenger);
    }

    @Test
    void GivenOneDriver_WhenStartMatching_ShouldMatchThatDriver() throws Exception {
        givenOneDriver();
        MatchView matchView = startMatching();
        shouldMatchThatDriver(matchView);
    }


    private MatchView startMatching() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(preferences);
        return getBody(
                mockMvc.perform(post("/api/users/{passengerId}/match", passenger.getId())
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful()), MatchView.class);
    }

    @SuppressWarnings("BusyWait")
    private void shouldMatchThatDriver(MatchView matchView) throws Exception {
        final int timeCountdown = 10000;
        MatchView pollMatch = pollCompletedMatch(timeCountdown, matchView);
        if (!pollMatch.completed) {
            throw new AssertionError("The match can't be completed within " + timeCountdown + "ms.");
        }
        Assertions.assertEquals(
                MatchView.DriverView.fromEntity(driver), pollMatch.driver, "The matched driver is incorrect.");
    }

    @Test
    void GivenOneDriver_WhenStartMatchingInParallel_ShouldHaveOnlyOneMatchCompleted() {
        givenOneDriver();

        List<MatchView> matchViews = Collections.synchronizedList(new LinkedList<>());
        IntStream.range(0, 8).parallel()
                .mapToObj((i) -> sneakyThrows(this::startMatching))
                .map(matchView -> sneakyThrows(() -> pollCompletedMatch(8000, matchView)))
                .forEach(matchViews::add);

        long completionCount = matchViews.stream()
                .filter(MatchView::isCompleted)
                .count();

        assertEquals(1, completionCount, "Race Condition: Found multiple match-completion but there is only one driver.");
    }

    private MatchView pollCompletedMatch(long timeCountdown, MatchView matchView) throws Exception {
        MatchView pollMatch;
        do {
            Thread.sleep(800);
            timeCountdown -= 800;
            pollMatch = getBody(mockMvc.perform(get("/api/users/{passengerId}/match/{matchId}",
                    passenger.getId(), matchView.id))
                    .andExpect(status().isOk()), MatchView.class);
        } while (!pollMatch.completed && timeCountdown >= 0);
        return pollMatch;
    }

    private void givenOneDriver() {
        userServiceDriver.addDriver(driver);
    }

}