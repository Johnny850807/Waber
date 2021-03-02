package tw.waterball.ddd.waber.pricing.repositories;

import tw.waterball.ddd.model.payment.Payment;

import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByTripId(String tripId);
    boolean existsByTripId(String tripId);
}
