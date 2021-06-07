package tw.waterball.ddd.waber.springboot.payment.controllers;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.api;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.event;

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
        currentSpan(api("CheckoutPayment"), attr("tripId", tripId));

        var presenter = new PaymentPresenter();
        checkoutPayment.execute(new CheckoutPayment.Request(tripId), presenter);
        return presenter.getPaymentView();
    }
}
