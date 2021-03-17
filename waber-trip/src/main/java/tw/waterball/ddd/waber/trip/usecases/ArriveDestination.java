package tw.waterball.ddd.waber.trip.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.TripStateChangedEvent;
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
    private final FindCurrentTrip findCurrentTrip;
    private final PaymentServiceDriver paymentServiceDriver;
    private final TripRepository tripRepository;

    @WithSpan
    public void execute(Request req, EventBus eventBus) {
        var result = findCurrentTrip.executeAndGetResult(new FindCurrentTrip.Request(req.passengerId));

        Trip trip = result.getTrip();
        trip.arrive();
        tripRepository.saveTripWithMatch(trip, result.getMatch());

        eventBus.publish(new TripStateChangedEvent(result.getMatch(), trip));
        paymentServiceDriver.checkoutPayment(trip.getId());
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
    }
}
