package tw.waterball.ddd.waber.match.usecases;

import static java.util.Optional.ofNullable;

import lombok.AllArgsConstructor;
import tw.waterball.ddd.commons.exceptions.NotFoundException;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.model.user.User;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;
import tw.waterball.ddd.waber.match.repositories.MatchRepository;

import javax.inject.Named;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
@AllArgsConstructor
public class FindCurrentMatch {
    private final UserServiceDriver userServiceDriver;
    private final MatchRepository matchRepository;

    public Optional<Match> execute(int userId) {
        DefaultPresenter defaultPresenter = new DefaultPresenter();
        execute(userId, defaultPresenter);
        return defaultPresenter.getMatch();
    }

    public void execute(int userId, Presenter presenter) {
        User user = userServiceDriver.getUser(userId);
        Supplier<Optional<Match>> findMatch = user instanceof Passenger ?
                () -> matchRepository.findPassengerCurrentMatch(userId) :
                () -> matchRepository.findDriverCurrentMatch(userId);
        findMatch.get().ifPresent(presenter::setMatch);
    }

    public interface Presenter {
        void setMatch(Match match);
    }

    public class DefaultPresenter implements Presenter {
        private Match match;
        @Override
        public void setMatch(Match match) {
            this.match = match;
        }
        public Optional<Match> getMatch() {
            return ofNullable(match);
        }
    }
}
