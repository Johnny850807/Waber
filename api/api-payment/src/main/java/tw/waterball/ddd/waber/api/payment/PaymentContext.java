package tw.waterball.ddd.waber.api.payment;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface PaymentContext {
    PaymentView checkoutPayment(String tripId);
}
