package tw.waterball.ddd.api.match;

import tw.waterball.ddd.model.match.MatchPreferences;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface MatchContext {

    MatchView startMatching(int userId, MatchPreferences matchPreferences);

    MatchView getMatch(int matchId);

    MatchView getCurrentMatch(int userId);
}
