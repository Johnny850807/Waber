package tw.waterball.ddd.waber.springboot.trip.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.waber.springboot.trip.presenters.TripPresenter;
import tw.waterball.ddd.waber.trip.usecases.StartTrip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
@RequestMapping("/api/users/{passengerId}/match/{matchId}/trip")
@RestController
public class TripController {
    private StartTrip startTrip;

    @PostMapping
    public TripView startTrip(@PathVariable int passengerId,
                              @PathVariable int matchId) {
        var presenter = new TripPresenter();
        startTrip.execute(new StartTrip.Request(passengerId, matchId), presenter);
        return presenter.getTripView();
    }
}
