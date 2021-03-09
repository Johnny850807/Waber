package tw.waterball.ddd.waber.pricing.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.trip.TripServiceDriver;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.payment.Payment;
import tw.waterball.ddd.model.payment.PricingStrategy;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.pricing.repositories.PaymentRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
public class CheckoutPayment {
    private final PricingStrategy pricingStrategy;
    private final MatchServiceDriver matchServiceDriver;
    private final TripServiceDriver tripServiceDriver;
    private final PaymentRepository paymentRepository;

    public CheckoutPayment(PricingStrategy pricingStrategy,
                           MatchServiceDriver matchServiceDriver,
                           TripServiceDriver tripServiceDriver,
                           PaymentRepository paymentRepository) {
        this.pricingStrategy = pricingStrategy;
        this.matchServiceDriver = matchServiceDriver;
        this.tripServiceDriver = tripServiceDriver;
        this.paymentRepository = paymentRepository;
    }

    public void execute(Request request, Presenter presenter) {
        Trip trip = getTrip(request);
        if (paymentRepository.existsByTripId(trip.getId())) {
            throw new IllegalStateException("The trip's payment has been checked out.");
        }
        Payment payment = new Payment();
        payment.checkout(trip, pricingStrategy);
        payment = paymentRepository.save(payment);
        presenter.present(payment);
    }

    private Trip getTrip(Request request) {
        TripView tripView = tripServiceDriver.getTrip(request.tripId);
        MatchView matchView = matchServiceDriver.getMatch(tripView.matchId);
        return tripView.toEntity(matchView.toEntity());
    }

    @AllArgsConstructor
    public static class Request {
        public String tripId;
    }

    public interface Presenter {
        void present(Payment payment);
    }
}
