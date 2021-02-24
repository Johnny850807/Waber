package tw.waterball.ddd.waber.springboot.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.stubs.UserStubs;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ContextConfiguration(classes = ObjectMapperConfiguration.class)
class ObjectMapperConfigurationTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testDeserializePassenger() throws JsonProcessingException {
        Passenger passenger = UserStubs.NORMAL_PASSENGER;
        String json = objectMapper.writeValueAsString(passenger);
        User user = objectMapper.readValue(json, User.class);
        assertEquals(Passenger.TYPE, user.getType());
        assertEquals(passenger.getEmail(), user.getEmail());
    }
    @Test
    void testDeserializeDriver() throws JsonProcessingException {
        Driver driver = UserStubs.NORMAL_DRIVER;
        String json = objectMapper.writeValueAsString(driver);
        Driver user = (Driver) objectMapper.readValue(json, User.class);
        assertEquals(Driver.TYPE, user.getType());
        assertEquals(driver.getEmail(), user.getEmail());
        assertEquals(driver.getCarType(), user.getCarType());

    }
}