package tw.waterball.ddd.stubs;

import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.trip.Trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class TripStubs {
    public static Trip PICKING_TRIP = new Trip("999", MatchStubs.COMPLETED_MATCH.getId());
    public static Trip DRIVING_TRIP;

    static {
        DRIVING_TRIP = new Trip("999", MatchStubs.COMPLETED_MATCH.getId());
        DRIVING_TRIP.startDriving(new Location(400, 400));
    }

    public static Trip ARRIVED_TRIP;

    static {
        ARRIVED_TRIP = new Trip("999", MatchStubs.COMPLETED_MATCH.getId());
        ARRIVED_TRIP.startDriving(new Location(400, 400));
        ARRIVED_TRIP.arrive();
    }
}
