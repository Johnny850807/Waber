package tw.waterball.ddd.api.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Builder
public class MatchView {
    public int id;
    public int passengerId;
    public DriverView driver;
    public boolean completed;
    public MatchPreferences matchPreferences;

    public static MatchView fromEntity(Match match) {
        return MatchView.builder()
                .id(match.getId())
                .passengerId(match.getId())
                .driver(match.getDriver()
                        .map(DriverView::fromEntity).orElse(null))
                .completed(match.isCompleted())
                .matchPreferences(match.getPreferences())
                .build();
    }

    @AllArgsConstructor
    public static class DriverView {
        public Integer id;
        public String name;

        public static DriverView fromEntity(Driver driver) {
            return new DriverView(driver.getId(), driver.getName());
        }
    }
}
