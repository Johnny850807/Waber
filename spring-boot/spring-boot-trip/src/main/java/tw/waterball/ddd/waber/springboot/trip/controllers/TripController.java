package tw.waterball.ddd.waber.springboot.trip.controllers;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.waber.springboot.trip.presenters.TripPresenter;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;
import tw.waterball.ddd.waber.trip.usecases.ArriveDestination;
import tw.waterball.ddd.waber.trip.usecases.FindCurrentTrip;
import tw.waterball.ddd.waber.trip.usecases.StartDriving;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api")
@RestController
public class TripController {
    private final StartDriving startDriving;
    private final ArriveDestination arriveDestination;
    private final FindCurrentTrip findCurrentTrip;
    private final TripRepository tripRepository;
    private final EventBus eventBus;

    @GetMapping("/trips/health")
    public String health() {
        return "OK";
    }

    @PatchMapping("/users/{userId}/trips/current/startDriving")
    public void startDriving(@PathVariable int userId,
                             @RequestBody Location destination) {
        currentSpan(event("TripStartDriving"), attr("userId", userId),
                attr("destinationLatitude", destination.getLatitude()),
                attr("destinationLongitude", destination.getLongitude()))
                .asLog(log::info);

        startDriving.execute(new StartDriving.Request(userId, destination));
    }

    @PatchMapping("/users/{userId}/trips/current/arrive")
    public void arrive(@PathVariable int userId) {
        currentSpan(event("TripArrive"), attr("userId", userId)).asLog(log::info);
        arriveDestination.execute(new ArriveDestination.Request(userId));
    }

    @GetMapping("/trips/{tripId}")
    public TripView getTripById(@PathVariable String tripId) {
        return tripRepository.findById(tripId)
                .map(TripView::toViewModel)
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/users/{userId}/trips/current")
    public TripView getCurrentTrip(@PathVariable int userId) {
        TripPresenter tripPresenter = new TripPresenter();
        findCurrentTrip.execute(new FindCurrentTrip.Request(userId), tripPresenter);
        return tripPresenter.getTripView();
    }

}
