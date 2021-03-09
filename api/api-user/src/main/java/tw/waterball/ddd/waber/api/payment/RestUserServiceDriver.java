package tw.waterball.ddd.waber.api.payment;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverIsNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestUserServiceDriver implements UserServiceDriver {
    private final BaseUrl userServiceBaseUrl;
    private final RestTemplate api;

    @Override
    public Driver getDriver(int driverId) {
        User user = getUser(driverId);
        if (!(user instanceof Driver)) {
            throw new IllegalStateException("User (id=" + driverId + ") is not a driver.");
        }
        return (Driver) user;
    }

    @Override
    public Passenger getPassenger(int passengerId) {
        User user = getUser(passengerId);
        if (!(user instanceof Passenger)) {
            throw new IllegalStateException("User (id=" + passengerId + ") is not a passenger.");
        }
        return (Passenger) user;
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

    @Override
    public User getUser(int userId) {
        return api.getForEntity(userServiceBaseUrl.withPath("/api/users/" + userId),
                User.class).getBody();
    }

    @Override
    public void setDriverStatus(int driverId, Driver.Status status) throws DriverIsNotAvailableException {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        var httpEntity = new HttpEntity<>(status.toString(), headers);

        api.exchange(userServiceBaseUrl.withPath("/api/drivers/" + driverId),
                HttpMethod.PATCH, httpEntity, String.class);
    }
}