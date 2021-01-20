package tw.waterball.ddd.waber.springboot.payment.repositories.jpa;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.model.payment.Payment;
import tw.waterball.ddd.waber.pricing.repositories.PaymentRepository;

import javax.inject.Named;

import static tw.waterball.ddd.waber.springboot.payment.repositories.jpa.PaymentData.toData;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class SpringBootPaymentRepository implements PaymentRepository {
    private MongoPaymentDataPort dataPort;

    @Override
    public Payment save(Payment payment) {
        PaymentData savedData = dataPort.save(toData(payment));
        return savedData.toEntity();
    }
}
