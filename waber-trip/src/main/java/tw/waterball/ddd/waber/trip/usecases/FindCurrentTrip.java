package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.TripStateChangedEvent;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.api.payment.PaymentServiceDriver;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class FindCurrentTrip {
    private final MatchServiceDriver matchServiceDriver;
    private final TripRepository tripRepository;

    public Trip execute(int userId) {
        return execute(userId, false);
    }

    public Trip execute(int userId, boolean startNewTripIfNotFound) {
        Match match = getCurrentMatch(userId);
        var findTrip = tripRepository.findByMatchId(match.getId());
        Trip trip = startNewTripIfNotFound ? findTrip.orElseGet(() -> startNewTrip(match))
                :findTrip.orElseThrow(NotFoundException::new);
        trip.getMatchAssociation().resolveAssociation(match);
        return trip;
    }

    private Trip startNewTrip(Match match) {
        Trip trip = new Trip(match);
        return tripRepository.save(trip);
    }

    private Match getCurrentMatch(int userId) {
        MatchView matchView = matchServiceDriver.getCurrentMatch(userId);
        return matchView.toEntity();
    }

}
