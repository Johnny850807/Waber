package tw.waterball.ddd.waber.springboot.user.controllers;

import org.junit.jupiter.api.Test;
import tw.waterball.ddd.model.user.Driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;

class DriverControllerTest extends AbstractUserApplicationTest {

    @Test
    void testSignUpDriver() throws Exception {
        Driver actual = signUpDriver();

        assertNotNull(actual.getId());
        driver.setId(actual.getId());
        assertEquals(toData(driver), toData(actual));
    }
}