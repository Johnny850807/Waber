package tw.waterball.ddd.api.trip;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface TripServiceDriver {
    TripView getTrip(String tripId);
}
