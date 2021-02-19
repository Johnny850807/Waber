package tw.waterball.ddd.waber.springboot.trip.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.waber.springboot.trip.presenters.TripPresenter;
import tw.waterball.ddd.waber.trip.usecases.ArriveDestination;
import tw.waterball.ddd.waber.trip.usecases.StartDriving;
import tw.waterball.ddd.waber.trip.usecases.StartTrip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/users/{passengerId}/matches/{matchId}/trips")
@RestController
public class TripController {
    private StartTrip startTrip;
    private StartDriving startDriving;
    private ArriveDestination arriveDestination;
    private EventBus eventBus;


    @GetMapping("/health")
    public String health(@PathVariable int passengerId,
                         @PathVariable int matchId) {
        return "OK";
    }

    @PostMapping
    public TripView startTrip(@PathVariable int passengerId,
                              @PathVariable int matchId) {
        var presenter = new TripPresenter();
        startTrip.execute(new StartTrip.Request(passengerId, matchId), presenter);
        return presenter.getTripView();
    }

    @PatchMapping("/{tripId}/drive")
    public void startDriving(@PathVariable int passengerId,
                             @PathVariable int matchId,
                             @PathVariable String tripId,
                             @RequestBody Location destination) {
        startDriving.execute(new StartDriving.Request(passengerId, matchId, tripId, destination));
    }

    @PatchMapping("/{tripId}/arrive")
    public void arrive(@PathVariable int passengerId,
                       @PathVariable int matchId,
                       @PathVariable String tripId) {
        arriveDestination.execute(new ArriveDestination.Request(passengerId, matchId, tripId),
                eventBus);
    }


}
