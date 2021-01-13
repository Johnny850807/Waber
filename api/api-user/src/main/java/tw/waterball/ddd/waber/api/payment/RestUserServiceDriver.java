package tw.waterball.ddd.waber.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverHasBeenMatchedException;
import tw.waterball.ddd.model.user.Passenger;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestUserServiceDriver implements UserServiceDriver {
    private ObjectMapper objectMapper;
    private BaseUrl userServiceBaseUrl;
    private RestTemplate api;

    @Override
    public Driver getDriver(int driverId) {
        return api.getForEntity(userServiceBaseUrl.withPath("/api/users/" + driverId),
                Driver.class).getBody();
    }

    @Override
    public Passenger getPassenger(int passengerId) {
        return api.getForEntity(userServiceBaseUrl.withPath("/api/users/" + passengerId),
                Passenger.class).getBody();
    }

    @Override
    public List<Driver> filterDrivers(MatchPreferences matchPreferences) {
        Driver[] drivers = api.getForEntity(
                fromHttpUrl(userServiceBaseUrl.withPath("/api/drivers"))
                        .queryParam("activityName", matchPreferences.getActivityName())
                        .queryParam("carType", matchPreferences.getCarType())
                        .toUriString(), Driver[].class).getBody();
        return asList(requireNonNull(drivers));
    }

    @SneakyThrows
    @Override
    public void setDriverStatus(int driverId, Driver.Status status) throws DriverHasBeenMatchedException {
        var httpEntity = new HttpEntity<>(status.toString());
        httpEntity.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

        api.exchange(userServiceBaseUrl.withPath("/api/drivers/" + driverId),
                 HttpMethod.PATCH, httpEntity, String.class);
    }
}