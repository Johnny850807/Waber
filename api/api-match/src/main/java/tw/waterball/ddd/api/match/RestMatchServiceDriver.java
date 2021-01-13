package tw.waterball.ddd.api.match;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestMatchServiceDriver implements MatchServiceDriver {
    private BaseUrl matchServiceBaseUrl;
    private RestTemplate api;

    @Override
    public MatchView getMatch(int passengerId, int matchId) {
        return api.getForEntity(
                matchServiceBaseUrl.withPath("/api/users/{passengerId}/match/{matchId}"),
                MatchView.class, passengerId, matchId).getBody();
    }
}
