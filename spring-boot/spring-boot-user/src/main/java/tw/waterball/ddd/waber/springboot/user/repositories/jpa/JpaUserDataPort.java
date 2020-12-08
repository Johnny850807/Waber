package tw.waterball.ddd.waber.springboot.user.repositories.data.ports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.waterball.ddd.waber.springboot.user.repositories.data.UserData;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Repository
public interface JpaUserDataPort extends JpaRepository<UserData, Integer> {
}
