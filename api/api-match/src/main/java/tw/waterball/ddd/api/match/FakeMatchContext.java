package tw.waterball.ddd.api.match;

import tw.waterball.ddd.model.match.MatchPreferences;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class FakeMatchContext implements MatchContext {
    public Map<Integer, MatchView> matchViews;

    public FakeMatchContext() {
        this(Collections.emptySet());
    }

    public FakeMatchContext(Set<MatchView> matchViews) {
        this.matchViews = matchViews.stream().collect(Collectors.toMap(MatchView::getId, m -> m));
    }

    public void addMatchView(MatchView matchView) {
        matchViews.put(matchView.getId(), matchView);
    }

    @Override
    public MatchView startMatching(int userId, MatchPreferences matchPreferences) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MatchView getMatch(int matchId) {
        return matchViews.get(matchId);
    }

    @Override
    public MatchView getCurrentMatch(int userId) {
        return matchViews.values()
                .stream().filter(m -> m.passengerId == userId || m.driver.id == userId)
                .findFirst()
                .orElse(null);
    }
}
