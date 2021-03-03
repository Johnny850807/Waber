package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Repository
public interface JpaMatchDataPort extends JpaRepository<MatchData, Integer> {
    Optional<MatchData> findFirstByPassengerIdAndAliveIsTrueOrderByCreatedDateDesc(int passengerId);
    Optional<MatchData> findFirstByDriverIdAndAliveIsTrueOrderByCreatedDateDesc(int driverId);

    @Modifying
    @Transactional
    @Query("UPDATE MatchData m SET m.alive = ?2 WHERE m.id = ?1")
    void setAlive(int matchId, boolean alive);
}