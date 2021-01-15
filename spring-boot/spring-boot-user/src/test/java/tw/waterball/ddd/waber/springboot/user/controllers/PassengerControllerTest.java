package tw.waterball.ddd.waber.springboot.user.controllers;

import org.junit.jupiter.api.Test;
import tw.waterball.ddd.model.user.Passenger;

import static org.junit.jupiter.api.Assertions.*;
import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;

class PassengerControllerTest extends AbstractUserApplicationTest {


    @Test
    void testSignUpPassenger() throws Exception {
        Passenger actual = signUpPassenger();

        assertNotNull(actual.getId());
        passenger.setId(actual.getId());
        assertEquals(toData(passenger), toData(actual));
    }

}