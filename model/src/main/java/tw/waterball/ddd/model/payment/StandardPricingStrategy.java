package tw.waterball.ddd.model.payment;

import io.opentelemetry.extension.annotations.WithSpan;
import tw.waterball.ddd.model.trip.Trip;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Named
public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public List<PricingItem> pricing(Trip trip) {
        // TODO: hard-coded pricing
        return Collections.singletonList(new PricingItem("StandardPricing",
                "Production's Price", BigDecimal.valueOf(3000)));
    }
}
