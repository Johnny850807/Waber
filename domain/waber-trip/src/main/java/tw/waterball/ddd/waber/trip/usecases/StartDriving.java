package tw.waterball.ddd.waber.trip.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.TripStateChangedEvent;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

import javax.inject.Named;

/**
 * This use-case has some performance issue,
 * this is designed on purpose to present the distributed tracing's convenience.
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class StartDriving {
    private final EventBus eventBus;
    private final FindCurrentTrip findCurrentTrip;
    private final TripRepository tripRepository;

    @WithSpan
    public void execute(Request req) {
        var result = findCurrentTrip.executeAndGet(
                new FindCurrentTrip.Request(req.passengerId, true));

        Trip trip = result.getTrip();
        trip.startDriving(req.destination);

        eventBus.publish(new TripStateChangedEvent(result.getMatch(), trip));
        tripRepository.saveTripWithMatch(trip, result.getMatch());
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
        public Location destination;
    }
}
