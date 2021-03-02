package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
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
    private final MatchServiceDriver matchServiceDriver;
    private final TripRepository tripRepository;

    public void execute(Request req) {
        Match match = getCurrentMatch(req.passengerId);
        Trip trip = tripRepository.findByMatchId(match.getId())
                .orElseGet(() -> startTrip(match));
        trip.getMatchAssociation().resolveAssociation(match);
        trip.startDriving(req.destination);
        tripRepository.save(trip);
    }

    private Trip startTrip(Match match) {
        Trip trip = new Trip(match);
        return tripRepository.save(trip);
    }

    private Match getCurrentMatch(int passengerId) {
        MatchView matchView = matchServiceDriver.getCurrentMatch(passengerId);
        return matchView.toEntity();
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
        public Location destination;
    }
}
