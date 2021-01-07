package tw.waterball.ddd.waber.match.repositories;

import tw.waterball.ddd.model.match.Match;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface MatchRepository {
    Match save(Match match);

    Optional<Match> findById(int matchId);

    Match associateById(int matchId);

}
