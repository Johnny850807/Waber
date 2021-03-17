package tw.waterball.ddd.waber.match.usecases;

import io.opentelemetry.extension.annotations.WithSpan;
import lombok.AllArgsConstructor;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import javax.inject.Named;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
@Named
public class FinalizeMatch {
    private final MatchRepository matchRepository;

    @WithSpan
    public void execute(int matchId) {
        matchRepository.setAlive(matchId, false);
    }
}
