package tw.waterball.ddd.waber.trip.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.TripStateChangedEvent;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.api.payment.PaymentContext;
import tw.waterball.ddd.waber.api.payment.UserContext;
import tw.waterball.ddd.waber.trip.repositories.TripRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class ArriveDestination {
    private final EventBus eventBus;
    private final FindCurrentTrip findCurrentTrip;
    private final PaymentContext paymentContext;
    private final TripRepository tripRepository;
    private final UserContext userContext;

    public void execute(Request request) {
        var query = currentTrip(request);
        Trip trip = query.getTrip();
        Match match = query.getMatch();

        trip.arrive();

        userContext.changeDriverStatus(match.getDriverId(), Driver.Status.AVAILABLE);
        tripRepository.saveTripWithMatch(trip, match);
        paymentContext.checkoutPayment(trip.getId());
        eventBus.publish(new TripStateChangedEvent(match, trip));
    }

    private DefaultTripPresenter currentTrip(Request req) {
        return findCurrentTrip.executeAndGet(new FindCurrentTrip.Request(req.passengerId));
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
    }
}
