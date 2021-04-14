package tw.waterball.ddd.waber.match.repositories;

import tw.waterball.ddd.model.match.Match;

import java.util.List;
import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface MatchRepository {
    List<Match> saveAll(Match ...matches);

    Match save(Match match);

    Optional<Match> findById(int matchId);

    Optional<Match> findPassengerCurrentMatch(int passengerId);
    Optional<Match> findDriverCurrentMatch(int driverId);


    Match associateById(int matchId);

    void setAlive(int matchId, boolean alive);

    void removeAll();
}
