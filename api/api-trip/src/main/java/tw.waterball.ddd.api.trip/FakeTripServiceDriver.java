package tw.waterball.ddd.api.trip;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class FakeTripServiceDriver implements TripServiceDriver {
    private Map<String, TripView> tripViews = new HashMap<>();

    public FakeTripServiceDriver() {
        this(Collections.emptySet());
    }

    public FakeTripServiceDriver(Set<TripView> tripViews) {
        this.tripViews = tripViews.stream().collect(Collectors.toMap(t -> t.id, t -> t));
    }

    public void addTripView(TripView tripView) {
        tripViews.put(tripView.id, tripView);
    }

    @Override
    public TripView getTrip(String tripId) {
        return tripViews.get(tripId);
    }
}
