package tw.waterball.ddd.waber.springboot.payment.repositories.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface PaymentDAO extends MongoRepository<PaymentData, String> {
    Optional<PaymentData> findFirstByTripId(String tripId);
    boolean existsByTripId(String tripId);

}
