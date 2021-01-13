package tw.waterball.ddd.waber.springboot.match.controllers;

import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.associations.Many;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.MatchingUseCase;
import tw.waterball.ddd.waber.match.usecases.MatchingUseCase.StartMatchingRequest;
import tw.waterball.ddd.waber.springboot.match.presenters.MatchViewPresenter;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@RequestMapping("/api/users/{passengerId}/match")
@RestController
public class MatchController {
    private MatchingUseCase matchingUseCase;
    private MatchRepository matchRepository;
    private UserServiceDriver userServiceDriver;

    public MatchController(MatchingUseCase matchingUseCase,
                           MatchRepository matchRepository,
                           UserServiceDriver userServiceDriver) {
        this.matchingUseCase = matchingUseCase;
        this.matchRepository = matchRepository;
        this.userServiceDriver = userServiceDriver;
    }

    @PostMapping
    public MatchView startMatching(@PathVariable int passengerId,
                               @RequestBody MatchPreferences matchPreferences) {
        Passenger passenger = userServiceDriver.getPassenger(passengerId);
        Many<Driver> drivers = Many.lazyOn(() -> userServiceDriver.filterDrivers(matchPreferences));
        var presenter = new MatchViewPresenter();
        matchingUseCase.execute(new StartMatchingRequest(passenger, drivers, matchPreferences), presenter);
        return presenter.getMatchView();
    }

    @GetMapping("/{matchId}")
    public MatchView getMatch(@PathVariable int matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(NotFoundException::new);
        Optional<Integer> driverId = match.getDriverAssociation().getId();
        Driver driver = driverId.map(userServiceDriver::getDriver).orElse(null);
        match.setDriver(driver);
        return MatchView.toViewModel(match);
    }

}
