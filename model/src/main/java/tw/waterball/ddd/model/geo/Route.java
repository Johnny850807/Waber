package tw.waterball.ddd.model.geo;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Usage:
 * Route r = Route.from(location1).to(location2);
 *
 * @author - johnny850807@gmail.com (Waterball)
 */
@Value
public class Route {
    private Location from;
    private Location to;

    public static Route.Builder from(Location from) {
        return new Route.Builder(from);
    }

    private Route(Location from, Location to) {
        this.from = from;
        this.to = to;
    }

    @AllArgsConstructor
    public static class Builder {
        private Location from;

        public Route to(Location to) {
            return new Route(from, to);
        }
    }



}
