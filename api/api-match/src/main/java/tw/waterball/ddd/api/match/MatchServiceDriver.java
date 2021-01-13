package tw.waterball.ddd.api.match;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface MatchServiceDriver {

    MatchView getMatch(int passengerId, int matchId);
}
