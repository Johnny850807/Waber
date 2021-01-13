package tw.waterball.ddd.waber.api.payment;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import tw.waterball.ddd.commons.model.BaseUrl;
import tw.waterball.ddd.model.match.MatchPreferences;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.DriverHasBeenMatchedException;
import tw.waterball.ddd.model.user.Passenger;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
public class RestPaymentServiceDriver implements PaymentServiceDriver {
    private BaseUrl paymentServiceBaseUrl;
    private RestTemplate api;

    @Override
    public PaymentView createPayment(int passengerId, int matchId, String tripId) {
        return api.postForEntity(
                paymentServiceBaseUrl.withPath("/api/users/{passengerId}/match/{matchId}/trip/{tripId}/payment"),
                null,
                PaymentView.class, passengerId, matchId, tripId).getBody();
    }
}