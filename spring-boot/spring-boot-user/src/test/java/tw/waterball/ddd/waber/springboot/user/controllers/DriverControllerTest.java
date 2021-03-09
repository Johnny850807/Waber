package tw.waterball.ddd.waber.springboot.user.controllers;

import org.junit.jupiter.api.Test;
import tw.waterball.ddd.model.user.Driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;

class DriverControllerTest extends AbstractUserApplicationTest {

    @Test
    void testSignUpDriver() throws Exception {
        Driver expected = driver;
        signUpDriver();

        assertNotNull(driver.getId());
        expected.setId(driver.getId());
        assertEquals(toData(expected), toData(driver));
    }
}