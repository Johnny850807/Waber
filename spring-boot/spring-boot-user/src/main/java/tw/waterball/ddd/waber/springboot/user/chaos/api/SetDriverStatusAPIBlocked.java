package tw.waterball.ddd.waber.springboot.user.chaos.api;

import lombok.extern.slf4j.Slf4j;
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
@Component @Slf4j
public class SetDriverStatusAPIBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "user.SetDriverStatusAPIBlocked";
    }

    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(4, 7, 9),
                negativeNumberAtPositions(2, 3), positiveNumberAtPositions(6, 13));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.user.controllers.DriverController.setDriverStatus(..))")
    public void before(JoinPoint joinPoint) {
        log.trace("Chaos CUT");
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}
