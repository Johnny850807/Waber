package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Repository
public interface JpaUserDataPort extends JpaRepository<UserData, Integer> {
    Optional<UserData> findByEmailAndPassword(String email, String password);
}
