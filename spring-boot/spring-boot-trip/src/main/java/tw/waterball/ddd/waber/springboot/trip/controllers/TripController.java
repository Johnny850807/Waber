package tw.waterball.ddd.waber.springboot.trip.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.waber.trip.usecases.ArriveDestination;
import tw.waterball.ddd.waber.trip.usecases.StartDriving;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api")
@RestController
public class TripController {
    private final StartDriving startDriving;
    private final ArriveDestination arriveDestination;
    private final EventBus eventBus;


    @GetMapping("/trips/health")
    public String health() {
        return "OK";
    }

    @PatchMapping("/users/{passengerId}/trips/current/startDriving")
    public void startDriving(@PathVariable int passengerId,
                             @RequestBody Location destination) {
        startDriving.execute(new StartDriving.Request(passengerId, destination));
    }

    @PatchMapping("/users/{passengerId}/trips/current/arrive")
    public void arrive(@PathVariable int passengerId) {
        arriveDestination.execute(new ArriveDestination.Request(passengerId),
                eventBus);
    }


}
