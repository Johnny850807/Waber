package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface JpaActivityDataPort extends JpaRepository<ActivityData, Integer> {
    Optional<ActivityData> findByName(String name);

}
