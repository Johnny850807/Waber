package tw.waterball.ddd.api.match;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestMatchServiceDriver implements MatchServiceDriver {
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
}
