package tw.waterball.ddd.api.trip;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;
import tw.waterball.ddd.model.geo.Location;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestTripServiceDriver implements TripServiceDriver {
    private final BaseUrl tripServiceBaseUrl;
    private final RestTemplate api;

    @Override
    public void startDrivingToDestination(int userId, Location destination) {
        api.patchForObject(tripServiceBaseUrl.withPath("/api/users/"+userId+"/trips/current/startDriving"), destination, Void.class);
    }

    @Override
    public void arrive(int userId) {
        api.patchForObject(tripServiceBaseUrl.withPath("/api/users/"+userId+"/trips/current/arrive"), null, Void.class);
    }

    @Override
    public TripView getTrip(String tripId) {
        return api.getForEntity(
                tripServiceBaseUrl.withPath("/api/trips/{tripId}"),
                TripView.class, tripId).getBody();
    }
}
