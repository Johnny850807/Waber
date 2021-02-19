package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Repository
public interface JpaMatchDataPort extends JpaRepository<MatchData, Integer> {
    Optional<MatchData> findFirstByPassengerIdOrderByCreatedDateDesc(int passengerId);
}