package tw.waterball.ddd.waber.api.payment;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;
import tw.waterball.ddd.model.geo.Location;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverNotAvailableException;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;

import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestUserContext implements UserContext {
    private final BaseUrl userServiceBaseUrl;
    private final RestTemplate api;

    @Override
    public void uploadLocation(int userId, Location location) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("latitude", String.valueOf(location.getLatitude()));
        map.add("longitude", String.valueOf(location.getLongitude()));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        api.put(userServiceBaseUrl.withPath("/api/users/"+userId+ "/location"), request);
    }

    @Override
    public Driver signUpAsDriver(String name, String email, String password, Driver.CarType carType) {
        return api.postForEntity(userServiceBaseUrl.withPath("/api/drivers"), new Driver(name, email, password, carType),
                Driver.class).getBody();
    }

    @Override
    public Passenger signUpAsPassenger(String name, String email, String password) {
        return api.postForEntity(userServiceBaseUrl.withPath("/api/passengers"), new Passenger(name, email, password),
                Passenger.class).getBody();
    }

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
    public List<Driver> getDrivers(MatchPreferences matchPreferences) {
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
    public void changeDriverStatus(int driverId, Driver.Status status) throws DriverNotAvailableException {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        var httpEntity = new HttpEntity<>(status.toString(), headers);

        api.exchange(userServiceBaseUrl.withPath("/api/drivers/" + driverId),
                HttpMethod.PATCH, httpEntity, String.class);
    }
}