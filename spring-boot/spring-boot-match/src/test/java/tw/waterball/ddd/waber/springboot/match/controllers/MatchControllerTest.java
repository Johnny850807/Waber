package tw.waterball.ddd.waber.springboot.match.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.user.UserServiceDriver;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.stubs.UserStubs;
import tw.waterball.ddd.waber.springboot.match.MatchApplication;

import java.io.UnsupportedEncodingException;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MatchApplication.class)
@AutoConfigureMockMvc
public class MatchControllerTest {
    private Driver driver = UserStubs.NORMAL_DRIVER;
    private Passenger passenger = UserStubs.NORMAL_PASSENGER;
    private MatchPreferences preferences = new MatchPreferences(
            passenger.getLatestLocation(), driver.getCarType(), null);

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserServiceDriver userServiceDriver;

    @BeforeEach
    void setup() {
        when(userServiceDriver.getPassenger(passenger.getId())).thenReturn(passenger);
    }

    @Test
    void GivenOneDriver_WhenStartMatching_ShouldMatchThatDriver() throws Exception {
        givenOneDriver();
        MatchView matchView = startMatching();
        shouldMatchThatDriver(matchView);
    }

    private void givenOneDriver() {
        when(userServiceDriver.filterDrivers(any())).thenReturn(singletonList(driver));
        when(userServiceDriver.getDriver(driver.getId())).thenReturn(driver);
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
        MatchView pollMatch;

        do {
            Thread.sleep(800);
            pollMatch = getBody(mockMvc.perform(get("/api/users/{passengerId}/match/{matchId}",
                    passenger.getId(), matchView.id))
                    .andExpect(status().isOk()), MatchView.class);
        } while (!pollMatch.completed);

        Assertions.assertEquals(
                MatchView.DriverView.fromEntity(driver), pollMatch.driver, "The matched driver is incorrect.");
    }

    private <T> T getBody(ResultActions resultActions, Class<T> type) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(resultActions
                .andReturn().getResponse().getContentAsString(Charsets.UTF_8), type);
    }

}