package tw.waterball.ddd.api.trip;

import tw.waterball.ddd.model.geo.Location;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface TripServiceDriver {
    void startDrivingToDestination(int userId, Location destination);
    void arrive(int userId);
    TripView getTrip(String tripId);
}
