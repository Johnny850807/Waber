package tw.waterball.ddd.api.match;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.Builder;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;

import java.util.Date;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchView {
    public int id;
    public int passengerId;
    public DriverView driver;
    public boolean completed;
    public MatchPreferences matchPreferences;
    public Date createdDate;
    public boolean alive;

    public static MatchView toViewModel(Match match) {
        return toViewModel(match, null);
    }
    public static MatchView toViewModel(Match match, @Nullable Driver driver) {
        return MatchView.builder()
                .id(match.getId())
                .passengerId(match.getPassengerId())
                .driver(DriverView.toViewModel(driver))
                .completed(match.isCompleted())
                .matchPreferences(match.getPreferences())
                .createdDate(match.getCreatedDate())
                .alive(match.isAlive())
                .build();
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public Match toEntity() {
        return new Match(id, passengerId, driver.id, matchPreferences, createdDate, alive);
    }

    public int getId() {
        return id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class DriverView {
        public Integer id;
        public String name;

        public static DriverView toViewModel(@Nullable Driver driver) {
            if (driver == null) {
                return null;
            }
            return new DriverView(driver.getId(), driver.getName());
        }
    }
}
