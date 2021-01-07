package tw.waterball.ddd.waber.springboot.match.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.associations.Many;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.MatchingUseCase;
import tw.waterball.ddd.waber.match.usecases.MatchingUseCase.StartMatchingRequest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@RequestMapping("/api/users/{passengerId}/match")
@RestController
@AllArgsConstructor
public class MatchController {
    private RestTemplate api;
    private MatchingUseCase matchingUseCase;
    private MatchRepository matchRepository;

    @PostMapping
    public MatchView startMatching(@PathVariable int passengerId,
                               @RequestBody MatchPreferences matchPreferences) {
        Passenger passenger = queryPassenger(passengerId);
        Many<Driver> drivers = Many.lazyOn(() -> queryMatchedDrivers(matchPreferences));
        Match match = matchingUseCase.execute(new StartMatchingRequest(passenger, drivers, matchPreferences));
        return MatchView.fromEntity(match);
    }

    @GetMapping("/{matchId}")
    public MatchView getMatch(@PathVariable int matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(NotFoundException::new);
        Optional<Integer> driverId = match.getDriverAssociation().getId();
        Driver driver = driverId.map(this::queryDriver).orElse(null);
        match.setDriver(driver);
        return MatchView.fromEntity(match);
    }

    private Driver queryDriver(int driverId) {
        return api.getForEntity("http://user-service/api/users/" + driverId,
                Driver.class).getBody();
    }
    private Passenger queryPassenger(int passengerId) {
        return api.getForEntity("http://user-service/api/users/" + passengerId,
                Passenger.class).getBody();
    }

    private List<Driver> queryMatchedDrivers(MatchPreferences matchPreferences) {
        Driver[] drivers = api.getForEntity(fromHttpUrl("http://user-service/api/drivers")
                .queryParam("activityName", matchPreferences.getActivityName())
                .queryParam("carType", matchPreferences.getCarType())
                .toUriString(), Driver[].class).getBody();
        return asList(requireNonNull(drivers));
    }
}
