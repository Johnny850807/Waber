package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Repository
public interface JpaMatchDataPort extends JpaRepository<MatchData, Integer> {

}