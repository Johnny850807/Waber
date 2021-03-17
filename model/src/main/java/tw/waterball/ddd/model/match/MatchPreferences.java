package tw.waterball.ddd.model.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Activity;
import tw.waterball.ddd.model.user.Driver;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchPreferences {
    private Location startLocation;
    private Driver.CarType carType;
    private String activityName;

}
