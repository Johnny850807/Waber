package tw.waterball.ddd.waber.springboot.user.chaos.eventbus;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.api.ChaosMiskillingException;
import tw.waterball.chaos.api.ChaosTriggeredException;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.ddd.events.EventBus;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect @Slf4j
@Configuration
public class EventBusPublishBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "user.EventBusBlocked";
    }

    @Override
    protected Criteria criteria() {
        return and(positiveNumberAtPositions(1, 5, 8, 12), areZeros(13, 14));
    }

    @Before("execution(* tw.waterball.ddd.events.EventBus.publish(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.trace("Chaos CUT");
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}
