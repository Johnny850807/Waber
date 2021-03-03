package tw.waterball.ddd.waber.trip.usecases;

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

    public void execute(Request req, EventBus eventBus) {
        Trip trip = findCurrentTrip.execute(req.passengerId);
        trip.arrive();
        tripRepository.save(trip);
        eventBus.publish(new TripStateChangedEvent(trip.getMatch(), trip));
        paymentServiceDriver.checkoutPayment(trip.getId());
    }

    @AllArgsConstructor
    public static class Request {
        public int passengerId;
    }
}
