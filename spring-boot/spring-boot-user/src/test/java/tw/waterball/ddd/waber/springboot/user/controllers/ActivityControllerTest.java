package tw.waterball.ddd.waber.springboot.user.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import tw.waterball.ddd.model.user.Driver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static tw.waterball.ddd.waber.springboot.user.repositories.jpa.UserData.toData;

class ActivityControllerTest extends AbstractUserApplicationTest {

    public static final String VALENTINES_DAY = "ValentinesDay";

    private List<Driver> getValentinesDayParticipants() throws Exception {
        return getBody(mockMvc.perform(get("/api/activities/{activityName}/drivers", VALENTINES_DAY))
                .andExpect(status().isOk()), new TypeReference<>() {
        });
    }

    @Test
    void GivenDriverSignedUp_WhenHeParticipatesValentinesDay_ShouldBeTheParticipantOfValentinesDay() throws Exception {
        signUpDriver();

        participateValentinesDay();

        List<Driver> drivers = getValentinesDayParticipants();

        assertEquals(1, drivers.size(), "Should have only one participant, which is the one that just signed up.");
        assertEquals(toData(driver), toData(drivers.get(0)));
    }

    private void participateValentinesDay() throws Exception {
        mockMvc.perform(post("/api/activities/{activityName}/drivers/{driverId}",
                VALENTINES_DAY, driver.getId()))
                .andExpect(status().isOk());
    }

}