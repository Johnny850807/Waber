package tw.waterball.ddd.waber.springboot.trip.chaos;

import static java.util.Collections.singletonList;
import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.model.payment.CompositePricingStrategy;
import tw.waterball.ddd.model.payment.PricingItem;
import tw.waterball.ddd.model.payment.PricingStrategy;
import tw.waterball.ddd.model.trip.Trip;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect
@Component
public class ChaosPricingStrategy extends Md5Chaos {
    @Override
    public String getName() {
        return "trip.ChaosPricingStrategy";
    }

    @Override
    protected Criteria criteria() {
        return or(and(areNotZeros(0, 15), areZeros(11, 14)),
                areZeros(1, 2, 3, 4, 5),
                positiveNumberAtPositions(11, 12, 15));
    }

    @Around("execution(* tw.waterball.ddd.waber.springboot.trip.config.PricingStrategyConfiguration.compositePricingStrategy(..))")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        CompositePricingStrategy strategy = (CompositePricingStrategy) joinPoint.proceed();
        return new CompositePricingStrategy(singletonList(
                trip -> {
                    if (isAlive()) {
                        return singletonList(new PricingItem("Chaos", "This pricing item is hacked", BigDecimal.valueOf(Long.MAX_VALUE)));
                    }
                    return strategy.pricing(trip);
                }
        ));
    }
}
