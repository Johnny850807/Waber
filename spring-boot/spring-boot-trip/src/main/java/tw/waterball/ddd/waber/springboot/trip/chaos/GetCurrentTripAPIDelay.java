package tw.waterball.ddd.waber.springboot.trip.chaos;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import lombok.SneakyThrows;
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
public class GetCurrentTripAPIDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "trip.GetCurrentTripAPIDelay";
    }

    @Override
    protected Criteria criteria() {
        return areZeros(5, 8);
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.trip.controllers.TripController.getCurrentTrip(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            delay(3000);
        }
    }
}
