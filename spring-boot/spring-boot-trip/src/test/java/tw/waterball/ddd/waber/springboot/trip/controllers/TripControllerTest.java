package tw.waterball.ddd.waber.springboot.trip.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.api.match.FakeMatchServiceDriver;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.trip.TripStateType;
import tw.waterball.ddd.model.trip.states.Arrived;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.stubs.UserStubs;
import tw.waterball.ddd.waber.api.payment.PaymentServiceDriver;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;
import tw.waterball.ddd.waber.springboot.trip.TripApplication;
import tw.waterball.ddd.waber.springboot.trip.repositories.jpa.SpringBootTripRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.api.match.MatchView.toViewModel;
import static tw.waterball.ddd.api.trip.TripView.toViewModel;

@ContextConfiguration(classes = {TripApplication.class, TripControllerTest.TestConfig.class})
@AutoConfigureMockMvc
class TripControllerTest extends AbstractSpringBootTest {
    Passenger passenger = UserStubs.NORMAL_PASSENGER;
    Driver driver = UserStubs.NORMAL_DRIVER;
    Match match;
    TripView tripView;

    @Autowired
    FakeMatchServiceDriver matchServiceDriver;
    @MockBean
    PaymentServiceDriver paymentServiceDriver;

    @Autowired
    SpringBootTripRepository tripRepository;


    @Configuration
    public static class TestConfig {
        @Primary
        @Bean
        public FakeMatchServiceDriver fakeMatchServiceDriver() {
            return new FakeMatchServiceDriver();
        }

    }

    @AfterEach
    void cleanUp() {
        tripRepository.clearAll();
    }

    @Test
    void GivenMatch_WhenStartTrip_TripShouldBeCreatedWithPickingState() throws Exception {
        givenMatch();
        startTrip();

        Trip trip = tripRepository.findById(this.tripView.id).orElseThrow();
        assertEquals(TripStateType.PICKING, trip.getState().getType());
    }

    @Test
    void GivenPickingTrip_WhenStartDriving_TripShouldBeInDrivingState() throws Exception {
        givenTrip(TripStateType.PICKING);
        startDriving(new Location(500, 500));

        Trip trip = tripRepository.findById(this.tripView.id).orElseThrow();
        assertEquals(TripStateType.DRIVING, trip.getState().getType());
    }

    @Test
    void GivenDrivingTrip_WhenArrive_TripShouldBeInArrivedState_AndPriceShouldBeResolved() throws Exception {
        givenTrip(TripStateType.DRIVING);
        arrive();

        Trip trip = tripRepository.findById(this.tripView.id).orElseThrow();
        assertEquals(TripStateType.ARRIVED, trip.getState().getType());

        verify(paymentServiceDriver).checkoutPayment(passenger.getId(), match.getId(), trip.getId());
    }

    @Test
    void Given2ArrivedTrip_WhenQueryTripHistoryByPassengerIdOrDriverId_ShouldGetThose2Trips() throws Exception {
        givenMatch();
        List<Trip> trips = Arrays.asList(new Trip(match), new Trip(match));
        trips.forEach(trip -> trip.setState(new Arrived(trip)));
        trips = trips.stream().map(tripRepository::save).collect(Collectors.toList());

        List<TripView> expected = trips.stream().map(TripView::toViewModel).collect(Collectors.toList());

        List<TripView> passengerTripHistory = queryTripHistory(passenger.getId());
        assertEquals(new HashSet<>(expected), new HashSet<>(passengerTripHistory));

        List<TripView> driverTripHistory = queryTripHistory(driver.getId());
        assertEquals(new HashSet<>(expected), new HashSet<>(driverTripHistory));
    }


    private void givenTrip(TripStateType stateType) {
        givenMatch();
        Trip trip = new Trip(match);
        trip.setState(stateType.toState(trip));
        this.tripView = toViewModel(tripRepository.save(trip));
    }

    private void givenMatch() {
        match = new Match(345, passenger.getId(), driver, new MatchPreferences());
        matchServiceDriver.addMatchView(toViewModel(match));
    }

    private void arrive() throws Exception {
        mockMvc.perform(
                patch("/api/users/{passengerId}/matches/{matchId}/trips/{tripId}/arrive",
                        passenger.getId(), match.getId(), tripView.id))
                .andExpect(status().isOk());
    }


    private void startDriving(Location destination) throws Exception {
        mockMvc.perform(
                patch("/api/users/{passengerId}/matches/{matchId}/trips/{tripId}/drive",
                        passenger.getId(), match.getId(), tripView.id)
                        .content(toJson(destination))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void startTrip() throws Exception {
        this.tripView = getBody(mockMvc.perform(
                post("/api/users/{passengerId}/matches/{matchId}/trips",
                        passenger.getId(), match.getId()))
                .andExpect(status().isOk()), TripView.class);
    }

    private List<TripView> queryTripHistory(int userId) throws Exception {
        return getBody(mockMvc.perform(
                get("/api/users/{userId}/trips", userId))
                .andExpect(status().isOk()), new TypeReference<>() {
        });
    }

}