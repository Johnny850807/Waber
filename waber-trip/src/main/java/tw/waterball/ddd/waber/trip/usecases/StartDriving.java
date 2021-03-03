package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.TripStateChangedEvent;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
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

    public void execute(Request req) {
        Trip trip = findCurrentTrip.execute(req.passengerId, true);
        trip.startDriving(req.destination);
        eventBus.publish(new TripStateChangedEvent(trip.getMatch(), trip));
        tripRepository.save(trip);
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
        public Location destination;
    }
}
