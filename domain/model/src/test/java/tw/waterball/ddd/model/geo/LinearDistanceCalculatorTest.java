package tw.waterball.ddd.model.geo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearDistanceCalculatorTest {
    private Route route = Route.from(new Location(300, 700))
            .to(new Location(1000, 1000));
    private LinearDistanceCalculator linearDistanceCalculator = new LinearDistanceCalculator();

    @Test
    void testRouteDistance() {
        double distance = linearDistanceCalculator.calculate(route);
        assertEquals(761, (int) distance);
    }

}