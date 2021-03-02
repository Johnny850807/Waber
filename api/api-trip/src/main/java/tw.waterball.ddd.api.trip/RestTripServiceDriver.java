package tw.waterball.ddd.api.trip;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestTripServiceDriver implements TripServiceDriver {
    private final BaseUrl tripServiceBaseUrl;
    private final RestTemplate api;

    @Override
    public TripView getTrip(String tripId) {
        return api.getForEntity(
                tripServiceBaseUrl.withPath("/api/trips/{tripId}"),
                TripView.class, tripId).getBody();
    }
}
