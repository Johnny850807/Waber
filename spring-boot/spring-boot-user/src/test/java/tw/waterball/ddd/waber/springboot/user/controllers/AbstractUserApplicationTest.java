package tw.waterball.ddd.waber.springboot.user.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.stubs.UserStubs;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;
import tw.waterball.ddd.waber.springboot.testkit.AbstractSpringBootTest;
import tw.waterball.ddd.waber.springboot.user.UserApplication;
import tw.waterball.ddd.waber.user.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ContextConfiguration(classes = {UserApplication.class})
public abstract class AbstractUserApplicationTest extends AbstractSpringBootTest {
    protected Driver driver = UserStubs.NORMAL_DRIVER;
    protected Passenger passenger = UserStubs.NORMAL_PASSENGER;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ActivityRepository activityRepository;

    @AfterEach
    void cleanUp() {
        activityRepository.clearAll();
        userRepository.clearAll();
    }

    protected Driver signUpDriver() throws Exception {
        Driver newDriver = getBody(mockMvc.perform(post("/api/drivers")
                .content(toJson(driver))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()), Driver.class);
        return newDriver;
    }

    protected Passenger signUpPassenger() throws Exception {
        return getBody(mockMvc.perform(post("/api/passengers")
                .content(toJson(passenger))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()), Passenger.class);
    }

    protected User getUser(int userId) throws Exception {
        return getBody(mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isOk()), User.class);
    }
}
