package tw.waterball.ddd.model.match;

import lombok.Value;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.user.Driver;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Value
public class MatchPreferences {
    private Location startLocation;

    public boolean isMatch(Driver driver) {
        return true;
    }
}
