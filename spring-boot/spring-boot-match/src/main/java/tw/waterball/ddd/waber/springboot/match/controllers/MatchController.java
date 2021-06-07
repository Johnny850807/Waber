package tw.waterball.ddd.waber.springboot.match.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.commons.utils.OpenTelemetryUtils;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;
import tw.waterball.ddd.waber.match.usecases.FindCurrentMatch;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase.StartMatchingRequest;
import tw.waterball.ddd.waber.springboot.match.presenters.MatchPresenter;

import java.util.Optional;
import java.util.function.Supplier;

import static tw.waterball.ddd.api.match.MatchView.toViewModel;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.event;

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
    private final FindCurrentMatch findCurrentMatch;
    private final MatchPresenter matchPresenter;

    @PostMapping("/users/{passengerId}/matches")
    public MatchView startMatching(@PathVariable int passengerId,
                                   @RequestBody MatchPreferences matchPreferences) {
        currentSpan(event("StartMatching"),
                attr("passengerId", passengerId),
                attr("startLocation", matchPreferences.getStartLocation()),
                attr("carType", matchPreferences.getCarType()),
                attr("activityName", matchPreferences.getActivityName()))
                .asLog(log::info);

        matchUseCase.execute(new StartMatchingRequest(passengerId, matchPreferences), matchPresenter);
        return matchPresenter.getMatchView();
    }

    @GetMapping("/users/{userId}/matches/current")
    public ResponseEntity<MatchView> getUserCurrentMatch(@PathVariable int userId) {
        MatchView match = findCurrentMatch.execute(userId)
                .map(this::toMatchView).orElse(null);
        if (match == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(match);
        }
    }

    @GetMapping("/matches/{matchId}")
    public MatchView getMatchById(@PathVariable int matchId) {
        return matchRepository.findById(matchId)
                .map(this::toMatchView)
                .orElseThrow(NotFoundException::new);
    }

    private MatchView toMatchView(Match match) {
        matchPresenter.present(match);
        return matchPresenter.getMatchView();
    }


}
