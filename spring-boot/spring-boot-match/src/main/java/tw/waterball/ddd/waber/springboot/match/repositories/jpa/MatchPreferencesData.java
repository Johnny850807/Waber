package tw.waterball.ddd.waber.springboot.match.repositories.jpa;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Embeddable
@AllArgsConstructor @NoArgsConstructor
public class MatchPreferencesData {
    @Embedded
    private Location startLocation;
    private Driver.CarType carType;
    private String activityName;

    public static MatchPreferencesData fromEntity(MatchPreferences m) {
        return new MatchPreferencesData(m.getStartLocation(), m.getCarType(), m.getActivityName());
    }

    public MatchPreferences toEntity() {
        return new MatchPreferences(startLocation, carType, activityName);
    }
}
