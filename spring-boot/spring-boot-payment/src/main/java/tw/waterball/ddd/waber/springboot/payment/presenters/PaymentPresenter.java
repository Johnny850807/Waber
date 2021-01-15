package tw.waterball.ddd.waber.springboot.payment.presenters;

import tw.waterball.ddd.model.payment.Payment;
import tw.waterball.ddd.waber.api.payment.PaymentView;
import tw.waterball.ddd.waber.pricing.usecases.CheckoutPayment;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class PaymentPresenter implements CheckoutPayment.Presenter {
    private PaymentView paymentView;

    @Override
    public void present(Payment payment) {
        paymentView = PaymentView.toViewModel(payment);
    }

    public PaymentView getPaymentView() {
        return paymentView;
    }
}
