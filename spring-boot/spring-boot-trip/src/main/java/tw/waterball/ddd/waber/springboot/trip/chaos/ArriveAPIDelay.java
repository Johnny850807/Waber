package tw.waterball.ddd.waber.springboot.trip.chaos;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.api.ChaosTriggeredException;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.ddd.commons.utils.DelayUtils;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect
@Component
public class ArriveAPIDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "trip.ArriveAPIDelay";
    }

    @Override
    protected Criteria criteria() {
        return or(areNotZeros(0, 15), negativeNumberAtPositions(10, 11));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.trip.controllers.TripController.arrive(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            delay(5000);
        }
    }
}
