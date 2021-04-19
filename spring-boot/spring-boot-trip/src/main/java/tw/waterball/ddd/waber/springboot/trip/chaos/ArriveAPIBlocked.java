package tw.waterball.ddd.waber.springboot.trip.chaos;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.api.ChaosTriggeredException;
import tw.waterball.chaos.core.md5.Md5Chaos;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect
@Component
public class ArriveAPIBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "trip.ArriveAPIBlocked";
    }

    @Override
    protected Criteria criteria() {
        return and(areZeros(0, 15), positiveNumberAtPositions(10, 11));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.trip.controllers.TripController.arrive(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}
