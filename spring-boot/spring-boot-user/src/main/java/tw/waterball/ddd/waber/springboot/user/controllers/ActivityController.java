package tw.waterball.ddd.waber.springboot.match.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.utils.SplitUtils;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.match.repositories.ActivityRepository;
import tw.waterball.ddd.waber.match.usecases.ParticipateActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@RequestMapping("/api/activities")
@RestController
public class ActivityController {
    private RestTemplate restTemplate;
    private ParticipateActivity participateActivity;
    private ActivityRepository activityRepository;

    public ActivityController(RestTemplate restTemplate, ParticipateActivity participateActivity,
                              ActivityRepository activityRepository) {
        this.restTemplate = restTemplate;
        this.participateActivity = participateActivity;
        this.activityRepository = activityRepository;
    }

    @PostMapping("/{activityName}/drivers/{driverId}")
    public void participateActivity(@PathVariable String activityName,
                                    @PathVariable int driverId) {
        Driver driver = restTemplate.getForEntity("http://user-service/api/users/" + driverId,
                Driver.class).getBody();

        participateActivity.execute(new ParticipateActivity.Request(
                activityName, driver
        ));
    }

    @GetMapping("/{activityName}/drivers")
    public Set<Driver> getActivityParticipants(@PathVariable String activityName) {
        Set<Driver> participants = activityRepository.findByName(activityName)
                .orElseThrow(() -> new IllegalArgumentException("Activity Not Found."))
                .getParticipantDrivers();

        Driver[] query = Arrays.asList(restTemplate.getForEntity("http://user-service/api/users/" +
                        SplitUtils.splitByComma(participants, Driver::getId),
                Driver[].class).getBody());

        return participants;
    }



}
