package tw.waterball.ddd.stubs;

import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class MatchStubs {
    public static Match COMPLETED_MATCH = new Match(20,
            UserStubs.NORMAL_PASSENGER.getId(),
            UserStubs.NORMAL_DRIVER, new MatchPreferences());
}
