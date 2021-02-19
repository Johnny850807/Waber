package tw.waterball.ddd.api.match;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.Builder;
import lombok.AllArgsConstructor;
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

    public static MatchView toViewModel(Match match) {
        return MatchView.builder()
                .id(match.getId())
                .passengerId(match.getPassengerAssociation().getId())
                .driver(match.getDriverOptional()
                        .map(DriverView::fromEntity).orElse(null))
                .completed(match.isCompleted())
                .matchPreferences(match.getPreferences())
                .createdDate(match.getCreatedDate())
                .build();
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public Match toEntity() {
        return new Match(id, passengerId, driver.id, matchPreferences, createdDate);
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

        public static DriverView fromEntity(Driver driver) {
            return new DriverView(driver.getId(), driver.getName());
        }
    }
}
