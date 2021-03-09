package tw.waterball.ddd.waber.springboot.match.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase.StartMatchingRequest;
import tw.waterball.ddd.waber.springboot.match.presenters.MatchPresenter;

import java.util.Optional;
import java.util.function.Supplier;

import static tw.waterball.ddd.api.match.MatchView.toViewModel;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Slf4j
@CrossOrigin
@RequestMapping("/api")
@RestController
@AllArgsConstructor
public class MatchController {
    private final MatchUseCase matchUseCase;
    private final MatchRepository matchRepository;
    private final UserServiceDriver userServiceDriver;

    @PostMapping("/users/{passengerId}/matches")
    public MatchView startMatching(@PathVariable int passengerId,
                                   @RequestBody MatchPreferences matchPreferences) {
        var presenter = new MatchPresenter();
        matchUseCase.execute(new StartMatchingRequest(passengerId, matchPreferences), presenter);
        return presenter.getMatchView();
    }

    @GetMapping("/users/{userId}/matches/current")
    public MatchView getUserCurrentMatch(@PathVariable int userId) {
        User user = userServiceDriver.getUser(userId);
        Supplier<Optional<Match>> findMatch = user instanceof Passenger ?
                () -> matchRepository.findPassengerCurrentMatch(userId) :
                () -> matchRepository.findDriverCurrentMatch(userId);
        return findMatch.get()
                .map(this::toMatchView)
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("/matches/{matchId}")
    public MatchView getMatchById(@PathVariable int matchId) {
        return matchRepository.findById(matchId)
                .map(this::toMatchView)
                .orElseThrow(NotFoundException::new);
    }

    private MatchView toMatchView(Match match) {
        Optional<Integer> driverId = match.mayHaveDriverId();
        Driver driver = driverId.map(userServiceDriver::getDriver).orElse(null);
        return toViewModel(match, driver);
    }

}
