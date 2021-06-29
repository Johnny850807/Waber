package tw.waterball.ddd.api.trip;

import tw.waterball.ddd.model.geo.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class FakeTripContext implements TripContext {
    private Map<String, TripView> tripViews = new HashMap<>();

    public FakeTripContext() {
        this(Collections.emptySet());
    }

    public FakeTripContext(Set<TripView> tripViews) {
        this.tripViews = tripViews.stream().collect(Collectors.toMap(t -> t.id, t -> t));
    }

    public void addTripView(TripView tripView) {
        tripViews.put(tripView.id, tripView);
    }

    @Override
    public void startDrivingToDestination(int userId, Location destination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void arrive(int userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TripView getCurrentTrip(int userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TripView getTrip(String tripId) {
        return tripViews.get(tripId);
    }
}
