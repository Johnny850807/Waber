package tw.waterball.ddd.waber.pricing.repositories;

import tw.waterball.ddd.model.payment.Payment;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface PaymentRepository {
    Payment save(Payment payment);
}
