package tw.waterball.ddd.api.match;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;
import tw.waterball.ddd.model.match.MatchPreferences;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestMatchContext implements MatchContext {
    private final BaseUrl matchServiceBaseUrl;
    private final RestTemplate api;

    @Override
    public MatchView getMatch(int matchId) {
        return api.getForEntity(
                matchServiceBaseUrl.withPath("/api/matches/{matchId}"),
                MatchView.class, matchId).getBody();
    }

    @Override
    public MatchView getCurrentMatch(int userId) {
        return api.getForEntity(
                matchServiceBaseUrl.withPath("/api/users/{userId}/matches/current"),
                MatchView.class, userId).getBody();
    }

    @Override
    public MatchView startMatching(int userId, MatchPreferences matchPreferences) {
        return api.postForEntity(matchServiceBaseUrl.withPath("/api/users/"+userId+"/matches"), matchPreferences,
                MatchView.class).getBody();
    }

}
