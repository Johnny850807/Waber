package tw.waterball.ddd.model.user;

import lombok.Getter;
import lombok.Setter;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Getter
public class Passenger extends User {
    private Match match;

    public Passenger(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Passenger(String name, String email, String password) {
        super(name, email, password);
    }

    public Match startMatching(MatchPreferences matchPreferences) {
        this.match = Match.start(this, matchPreferences);
        return match;
    }

    public void cancelMatching() {
        if (isMatching()) {
            match.cancel();
            match = null;
        }
    }

    public boolean isMatching() {
        return match != null;
    }


}
