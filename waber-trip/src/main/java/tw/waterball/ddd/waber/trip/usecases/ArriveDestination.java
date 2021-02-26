package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.TripArrivedEvent;
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
public class ArriveDestination {
    private final MatchServiceDriver matchServiceDriver;
    private final PaymentServiceDriver paymentServiceDriver;
    private final TripRepository tripRepository;

    public void execute(Request req, EventBus eventBus) {
        Match match = getCurrentMatch(req);
        Trip trip = tripRepository.findByMatchId(match.getId()).orElseThrow(NotFoundException::new);
        trip.getMatchAssociation().resolveAssociation(match);
        trip.arrive();
        tripRepository.save(trip);
        eventBus.publish(new TripArrivedEvent(match, trip));
        paymentServiceDriver.checkoutPayment(req.passengerId, match.getId(), trip.getId());
    }

    private Match getCurrentMatch(Request req) {
        MatchView matchView = matchServiceDriver.getCurrentMatch(req.passengerId);
        if (matchView == null) {
            throw new NotFoundException();
        }
        return matchView.toEntity();
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
    }
}
