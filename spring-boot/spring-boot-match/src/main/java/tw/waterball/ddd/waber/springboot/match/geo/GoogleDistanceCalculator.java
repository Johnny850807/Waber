package tw.waterball.ddd.waber.springboot.match.geo;

import tw.waterball.ddd.model.geo.DistanceCalculator;
import tw.waterball.ddd.model.geo.Route;

import javax.inject.Named;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class GoogleDistanceCalculator implements DistanceCalculator {
    @Override
    public double calculate(Route route) {
        // TODO
        return 0;
    }
}
