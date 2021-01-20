package tw.waterball.ddd.waber.springboot.payment.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.waterball.ddd.waber.api.payment.PaymentView;
import tw.waterball.ddd.waber.pricing.usecases.CheckoutPayment;
import tw.waterball.ddd.waber.springboot.payment.presenters.PaymentPresenter;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@RequestMapping("/api/passengers/{passengerId}/matches/{matchId}/trips/{tripId}/payment")
@RestController
public class PaymentController {
    private final CheckoutPayment checkoutPayment;

    public PaymentController(CheckoutPayment checkoutPayment) {
        this.checkoutPayment = checkoutPayment;
    }

    @PostMapping
    public PaymentView checkoutPayment(@PathVariable int passengerId,
                                     @PathVariable int matchId,
                                     @PathVariable String tripId) {
        var presenter = new PaymentPresenter();
        checkoutPayment.execute(new CheckoutPayment.Request(passengerId, matchId, tripId), presenter);
        return presenter.getPaymentView();
    }
}
