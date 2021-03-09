package tw.waterball.ddd.waber.springboot.user.controllers;

import org.junit.jupiter.api.Test;
import tw.waterball.ddd.model.user.Passenger;

import static org.junit.jupiter.api.Assertions.*;
import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;

class PassengerControllerTest extends AbstractUserApplicationTest {


    @Test
    void testSignUpPassenger() throws Exception {
        Passenger expected = passenger;
        signUpPassenger();

        assertNotNull(passenger.getId());
        expected.setId(passenger.getId());
        assertEquals(toData(expected), toData(passenger));
    }

}