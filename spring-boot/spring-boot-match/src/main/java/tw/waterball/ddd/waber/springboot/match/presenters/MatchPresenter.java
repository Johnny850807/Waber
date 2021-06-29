package tw.waterball.ddd.waber.springboot.match.presenters;

import static tw.waterball.ddd.api.match.MatchView.toViewModel;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import tw.waterball.ddd.api.match.MatchView;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.waber.api.payment.UserContext;
import tw.waterball.ddd.waber.match.usecases.MatchUseCase;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@RequiredArgsConstructor
public class MatchPresenter implements MatchUseCase.Presenter {
    private final UserContext userContext;
    private Match match;

    @Override
    public void present(Match match) {
        if (this.match != null) {
            throw new IllegalStateException("Reused presenter");
        }
        this.match = match;
    }

    public MatchView getMatchView() {
        return match.mayHaveDriverId()
                .map(userContext::getDriver)
                .map(Driver::getName)
                .map(driverName -> toViewModel(match, driverName))
                .orElse(MatchView.toViewModel(match));
    }
}
