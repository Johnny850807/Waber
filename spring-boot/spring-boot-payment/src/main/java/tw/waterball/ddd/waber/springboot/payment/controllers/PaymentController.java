package tw.waterball.ddd.waber.springboot.payment.controllers;

import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.waber.api.payment.PaymentView;
import tw.waterball.ddd.waber.pricing.usecases.CheckoutPayment;
import tw.waterball.ddd.waber.springboot.payment.presenters.PaymentPresenter;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@CrossOrigin
@RequestMapping("/api/payments")
@RestController
public class PaymentController {
    private final CheckoutPayment checkoutPayment;

    public PaymentController(CheckoutPayment checkoutPayment) {
        this.checkoutPayment = checkoutPayment;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/trips/{tripId}")
    public PaymentView checkoutPayment(@PathVariable String tripId) {
        var presenter = new PaymentPresenter();
        checkoutPayment.execute(new CheckoutPayment.Request(tripId), presenter);
        return presenter.getPaymentView();
    }
}
