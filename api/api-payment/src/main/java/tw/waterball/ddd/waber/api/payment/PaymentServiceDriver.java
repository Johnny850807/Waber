package tw.waterball.ddd.waber.api.payment;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface PaymentServiceDriver {
    PaymentView checkoutPayment(int passengerId, int matchId, String tripId);
}
