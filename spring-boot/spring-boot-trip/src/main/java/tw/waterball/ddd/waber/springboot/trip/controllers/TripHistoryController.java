package tw.waterball.ddd.waber.springboot.trip.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.waber.springboot.trip.repositories.jpa.SpringBootTripRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/users/{userId}/trips")
@RestController
public class TripHistoryController {
    private SpringBootTripRepository tripRepository;

    @GetMapping
    public List<TripView> queryTripHistory(@PathVariable int userId) {
        List<TripView> tripViews = tripRepository.queryPassengerTripHistory(userId).stream()
                .map(TripView::toViewModel)
                .collect(Collectors.toList());
        // the userid is either the passenger's id or the driver's
        // and the trip history either belongs a passenger or a driver
        if (tripViews.isEmpty()) {
            tripViews.addAll(tripRepository.queryDriverTripHistory(userId).stream()
                    .map(TripView::toViewModel)
                    .collect(Collectors.toList()));
        }
        return tripViews;
    }
}
