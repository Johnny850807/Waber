package tw.waterball.ddd.model.geo;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class LinearDistanceCalculator implements DistanceCalculator {
    @Override
    public double calculate(Route route) {
        double a = Math.abs(route.from.getLatitude() - route.to.getLatitude());
        double b = Math.abs(route.from.getLongitude() - route.to.getLongitude());
        return Math.sqrt(a*a + b*b);
    }
}
