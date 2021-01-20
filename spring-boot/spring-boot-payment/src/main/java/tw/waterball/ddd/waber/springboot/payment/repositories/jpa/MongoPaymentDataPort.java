package tw.waterball.ddd.waber.springboot.payment.repositories.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface MongoPaymentDataPort extends MongoRepository<PaymentData, String> {
}
