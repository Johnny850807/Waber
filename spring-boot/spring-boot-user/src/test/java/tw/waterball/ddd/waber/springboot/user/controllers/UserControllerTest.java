package tw.waterball.ddd.waber.springboot.user.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.springboot.user.controllers.UserController.SignInParams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;


public class UserControllerTest extends AbstractUserApplicationTest {

    @Test
    void GivenPassengerSignedUp_WhenSignIn_ShouldSucceed() throws Exception {
        signUpPassenger();

        SignInParams signInParams = new SignInParams(passenger.getEmail(), passenger.getPassword());
        Passenger actual = signIn(signInParams);

        assertNotNull(actual.getId());
        passenger.setId(actual.getId());
        assertEquals(toData(passenger), toData(actual));
    }

    @Test
    void GivenDriverSignedUp_WhenUpdateLocation_ShouldBeUpdated() throws Exception {
        driver = signUpDriver();

        Location location = new Location(500.44, 600.77);
        updateLocation(driver.getId(), location);

        User user = getUser(driver.getId());
        assertEquals(location, user.getLocation());
    }

    private void updateLocation(int userId, Location location) throws Exception {
        mockMvc.perform(put("/api/users/{userId}/location", userId)
                .queryParam("latitude", String.valueOf(location.getLatitude()))
                .queryParam("longitude", String.valueOf(location.getLongitude())))
                .andExpect(status().isOk());
    }

    private Passenger signIn(SignInParams signInParams) throws Exception {
        return getBody(mockMvc.perform(post("/api/users/signIn")
                .content(toJson(signInParams))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()), Passenger.class);
    }


}