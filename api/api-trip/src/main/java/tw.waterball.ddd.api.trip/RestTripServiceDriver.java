package tw.waterball.ddd.api.trip;

import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestTripServiceDriver implements TripServiceDriver {
    private BaseUrl matchServiceBaseUrl;
    private RestTemplate api;


    @Override
    public TripView getTrip(String tripId) {
        return null;
    }
}
