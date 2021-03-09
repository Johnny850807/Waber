package tw.waterball.ddd.waber.pricing.usecases;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.api.trip.TripServiceDriver;
import tw.waterball.ddd.api.trip.TripView;
import tw.waterball.ddd.model.payment.Payment;
import tw.waterball.ddd.model.payment.PricingItem;
import tw.waterball.ddd.model.payment.PricingStrategy;
import tw.waterball.ddd.model.trip.Trip;
import tw.waterball.ddd.waber.pricing.repositories.PaymentRepository;

import javax.inject.Named;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class CheckoutPayment {
    private final PricingStrategy pricingStrategy;
    private final TripServiceDriver tripServiceDriver;
    private final PaymentRepository paymentRepository;


    public void execute(Request request, Presenter presenter) {
        Trip trip = getTrip(request);
        if (paymentRepository.existsByTripId(trip.getId())) {
            throw new IllegalStateException("The trip's payment has been checked out.");
        }

        List<PricingItem> pricingItems = pricingStrategy.pricing(trip);
        Payment payment =  new Payment(trip.getId(), pricingItems);
        payment = paymentRepository.save(payment);
        presenter.present(payment);
    }

    private Trip getTrip(Request request) {
        TripView tripView = tripServiceDriver.getTrip(request.tripId);
        return tripView.toEntity();
    }

    @AllArgsConstructor
    public static class Request {
        public String tripId;
    }

    public interface Presenter {
        void present(Payment payment);
    }
}
