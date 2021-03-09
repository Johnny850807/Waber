package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.waterball.ddd.model.user.Driver;

import java.util.List;
import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Repository
public interface JpaUserDataPort extends JpaRepository<UserData, Integer> {
    Optional<UserData> findByEmail(String email);

    List<UserData> findAllByCarTypeAndDriverStatusIs(Driver.CarType carType, String driverStatus);

    List<UserData> findAllByDriverIsTrueAndDriverStatusIs(String driverStatus);
}
