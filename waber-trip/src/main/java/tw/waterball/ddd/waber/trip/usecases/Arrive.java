package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.geo.Location;
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
public class Arrive {
    private MatchServiceDriver matchServiceDriver;
    private PaymentServiceDriver paymentServiceDriver;
    private TripRepository tripRepository;

    public void execute(Request req) {
        Match match = getMatch(req);
        Trip trip = tripRepository.findById(req.tripId).orElseThrow(NotFoundException::new);
        trip.getMatchAssociation().resolveAssociation(match);
        trip.arrive();
        tripRepository.save(trip);
        paymentServiceDriver.createPayment(req.passengerId, req.matchId, trip.getId());
    }

    private Match getMatch(Request req) {
        MatchView matchView = matchServiceDriver.getMatch(req.passengerId, req.matchId);
        if (matchView == null || matchView.getId() != req.matchId) {
            throw new NotFoundException();
        }
        return matchView.toEntity();
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
        public int matchId;
        public String tripId;
    }
}
