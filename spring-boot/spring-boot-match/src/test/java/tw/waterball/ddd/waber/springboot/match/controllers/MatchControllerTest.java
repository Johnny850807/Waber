package tw.waterball.ddd.waber.springboot.match.controllers;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.utils.StreamUtils;
import tw.waterball.ddd.events.TripStateChangedEvent;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.TripStateType;
import tw.waterball.ddd.waber.api.payment.FakeUserServiceDriver;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.stubs.UserStubs;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.FinalizeMatch;
import tw.waterball.ddd.waber.springboot.commons.profiles.FakeServiceDrivers;
import tw.waterball.ddd.waber.springboot.match.MatchApplication;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.api.match.MatchView.DriverView.toViewModel;
import static tw.waterball.ddd.commons.utils.DelayUtils.delay;
import static tw.waterball.ddd.commons.utils.SneakyUtils.sneakyThrows;
import static tw.waterball.ddd.commons.utils.StreamUtils.count;
import static tw.waterball.ddd.waber.springboot.match.config.RabbitEventBusConfiguration.EVENTS_EXCHANGE;

@ActiveProfiles({FakeServiceDrivers.NAME})
@ContextConfiguration(classes = {MatchApplication.class, MatchControllerTest.TestConfig.class})
public class MatchControllerTest extends AbstractSpringBootTest {
    private final Driver driver = UserStubs.NORMAL_DRIVER;
    private final Passenger passenger = UserStubs.NORMAL_PASSENGER;
    private final MatchPreferences preferences = new MatchPreferences(
            passenger.getLocation(), driver.getCarType(), null);
    @Autowired
    MatchRepository matchRepository;

    @Autowired
    FakeUserServiceDriver userServiceDriver;

    @Autowired
    AmqpTemplate amqpTemplate;

    @SpyBean
    FinalizeMatch finalizeMatch;


    @Configuration
    public static class TestConfig {
        @Bean
        @Primary
        public ConnectionFactory mockRabbitMqConnectionFactory() {
            return new CachingConnectionFactory(new MockConnectionFactory());
        }
    }

    @BeforeEach
    public void setup() {
        userServiceDriver.addPassenger(passenger);
    }

    @AfterEach
    public void cleanUp() {
        matchRepository.removeAll();
        driver.setStatus(Driver.Status.AVAILABLE);
        userServiceDriver.reset();
    }

    @Test
    void GivenOneDriver_WhenStartMatching_ShouldMatchThatDriver() throws Exception {
        givenOneDriver();
        var match = startMatching();
        shouldMatchThatDriver(match);
    }

    @Test
    void GivenOneMatch_WhenTheTripHasBeenCanceled_TheMatchShouldBeFinalized_AndTheCurrentMatchOfPassengerIsNotPresent() throws Exception {
        Match match = givenOneMatch();

        tripHasArrivedDestination(match);

        awaitEventHandling();
        verify(finalizeMatch, times(1)).execute(match.getId());
        currentMatchShouldBeNotPresent();
    }

    @Test
    void GivenOneMatch_WhenTheTripHasArrivedTheDestination_TheMatchShouldBeFinalized_AndTheCurrentMatchOfPassengerIsNotPresent() throws Exception {
        Match match = givenOneMatch();

        tripHasArrivedDestination(match);

        awaitEventHandling();
        verify(finalizeMatch, times(1)).execute(match.getId());
        currentMatchShouldBeNotPresent();
    }

    @Test
    void GivenOneDriver_WhenMultipleMatchRequestsInParallel_ShouldHaveOnlyOneMatchCompleted() {
        givenOneDriver();

        var matches = issueMultipleMatchRequestsInParallel();

        long completionCount = count(matches, MatchView::isCompleted);
        assertNotEquals(0, completionCount, "The only driver should be matched.");
        assertEquals(1, completionCount, "Race Condition: Found multiple match-completion but there is only one driver.");
    }

    private List<MatchView> issueMultipleMatchRequestsInParallel() {
        return range(0, 4).parallel()
                .mapToObj((i) -> startMatching())
                .map(matchView -> pollCompletedMatch(8000, matchView))
                .collect(Collectors.toList());
    }


    private void givenOneDriver() {
        userServiceDriver.addDriver(driver);
    }

    @SneakyThrows
    private MatchView startMatching() {
        String jsonBody = objectMapper.writeValueAsString(preferences);
        return getBody(
                mockMvc.perform(post("/api/users/{passengerId}/matches", passenger.getId())
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful()), MatchView.class);
    }

    @SneakyThrows
    private MatchView pollCompletedMatch(long timeCountdown, MatchView matchView) {
        MatchView pollResult;
        do {
            Thread.sleep(800);
            timeCountdown -= 800;
            pollResult = getMatch(matchView.id);
        } while (!pollResult.completed && timeCountdown >= 0);
        return pollResult;
    }

    private MatchView getMatch(int matchId) throws Exception {
        return getBody(mockMvc.perform(get("/api/matches/{matchId}", matchId))
                .andExpect(status().isOk()), MatchView.class);
    }



    private void awaitEventHandling() {
        delay(3000); // naively await event handling
    }

    private void currentMatchShouldBePresent(Match match) throws Exception {
        getCurrentMatch().andExpect(status().isOk()).andExpect(jsonPath("id").value(match.getId()));
    }

    private void currentMatchShouldBeNotPresent() throws Exception {
        getCurrentMatch().andExpect(status().isNoContent());
    }

    private ResultActions getCurrentMatch() throws Exception {
        return mockMvc.perform(get("/api/users/{userId}/matches/current", passenger.getId()));
    }

    private Match givenOneMatch() throws Exception {
        givenOneDriver();
        Match match = matchRepository.save(Match.start(passenger.getId(),
                new MatchPreferences(new Location(0, 0), Driver.CarType.Normal, null))
                .matchDriver(driver));
        currentMatchShouldBePresent(match);
        return match;
    }

    private void tripHasArrivedDestination(Match match) {
        amqpTemplate.convertAndSend(EVENTS_EXCHANGE, "/trips/state/change",
                new TripStateChangedEvent(driver.getId(), passenger.getId(), match.getId(), "whatever", TripStateType.ARRIVED));
    }


    private void shouldMatchThatDriver(MatchView matchView) throws Exception {
        final int timeCountdown = 10000;
        MatchView pollMatch = pollCompletedMatch(timeCountdown, matchView);
        if (!pollMatch.completed) {
            throw new AssertionError("The match can't be completed within " + timeCountdown + "ms.");
        }
        assertEquals(toViewModel(driver), pollMatch.driver, "The matched driver is incorrect.");
    }

}