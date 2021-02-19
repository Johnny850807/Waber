package tw.waterball.ddd.waber.springboot.match.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.associations.Many;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.MatchingUseCase;
import tw.waterball.ddd.waber.match.usecases.MatchingUseCase.StartMatchingRequest;
import tw.waterball.ddd.waber.springboot.match.presenters.MatchPresenter;

import java.util.Optional;

import static tw.waterball.ddd.api.match.MatchView.toViewModel;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Slf4j
@CrossOrigin
@RequestMapping("/api/users/{passengerId}/matches")
@RestController
@AllArgsConstructor
public class MatchController {
    private MatchingUseCase matchingUseCase;
    private MatchRepository matchRepository;
    private UserServiceDriver userServiceDriver;

    @GetMapping("/health")
    public String health(@PathVariable int passengerId) {
        return "OK";
    }

    @PostMapping
    public MatchView startMatching(@PathVariable int passengerId,
                                   @RequestBody MatchPreferences matchPreferences) {
        Passenger passenger = userServiceDriver.getPassenger(passengerId);
        Many<Driver> drivers = Many.lazyOn(() -> userServiceDriver.filterDrivers(matchPreferences));
        var presenter = new MatchPresenter();
        matchingUseCase.execute(new StartMatchingRequest(passenger, drivers, matchPreferences), presenter);
        return presenter.getMatchView();
    }

    @GetMapping("/current")
    public MatchView getMatch(@PathVariable int passengerId) {
        return matchRepository.findPassengerCurrentMatch(passengerId)
                .map(this::toMatchView)
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/{matchId}")
    public MatchView getMatch(@PathVariable int passengerId,
                              @PathVariable int matchId) {
        return matchRepository.findById(matchId)
                .map(this::toMatchView)
                .orElseThrow(NotFoundException::new);
    }

    private MatchView toMatchView(Match match) {
        Optional<Integer> driverId = match.getDriverAssociation().getId();
        driverId.map(userServiceDriver::getDriver)
                .ifPresent(match::setDriver);
        return toViewModel(match);
    }

}
