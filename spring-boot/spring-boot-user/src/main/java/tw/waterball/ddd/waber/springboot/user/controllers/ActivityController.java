package tw.waterball.ddd.waber.springboot.user.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.passenger.repositories.ActivityRepository;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.waber.driver.usecases.ParticipateActivity;

import java.util.Collection;


/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@CrossOrigin
@RequestMapping("/api/activities")
@RestController
@AllArgsConstructor
public class ActivityController {
    private ParticipateActivity participateActivity;
    private ActivityRepository activityRepository;

    @PostMapping("/{activityName}/drivers/{driverId}")
    public void participateActivity(@PathVariable String activityName,
                                    @PathVariable int driverId) {
        participateActivity.execute(new ParticipateActivity.Request(
                activityName, driverId
        ));
    }

    @GetMapping("/{activityName}/drivers")
    public Collection<Driver> getActivityParticipants(@PathVariable String activityName) {
        Activity activity = activityRepository.findByName(activityName)
                .orElseThrow(NotFoundException::new);
        return activity.getParticipantDrivers();
    }


}
