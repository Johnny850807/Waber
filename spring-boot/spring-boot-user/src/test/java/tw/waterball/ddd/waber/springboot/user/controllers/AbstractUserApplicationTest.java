package tw.waterball.ddd.waber.springboot.user.controllers;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.model.user.Activity;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ActiveProfiles
@ContextConfiguration(classes = {UserApplication.class, AbstractUserApplicationTest.TestConfig.class})
public abstract class AbstractUserApplicationTest extends AbstractSpringBootTest {
    public static final String VALENTINES_DAY = "ValentinesDay";
    protected String password = "password";
    protected Driver driver = UserStubs.NORMAL_DRIVER;
    protected Passenger passenger = UserStubs.NORMAL_PASSENGER;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ActivityRepository activityRepository;


    @Configuration
    public static class TestConfig {
        @Bean
        @Primary
        public ConnectionFactory mockRabbitMqConnectionFactory() {
            return new CachingConnectionFactory(new MockConnectionFactory());
        }
    }

    @AfterEach
    void cleanUp() {
        activityRepository.removeAll();
        activityRepository.save(new Activity(VALENTINES_DAY));
        userRepository.removeAll();
    }

    protected void signUpDriver() throws Exception {
        driver = getBody(mockMvc.perform(post("/api/drivers")
                .content(toJson(driver))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("password").doesNotExist()), Driver.class);
    }

    protected void signUpPassenger() throws Exception {
        passenger = getBody(mockMvc.perform(post("/api/passengers")
                .content(toJson(passenger))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("password").doesNotExist()), Passenger.class);
    }

    protected User getUser(int userId) throws Exception {
        return getBody(mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("password").doesNotExist()), User.class);
    }

}

