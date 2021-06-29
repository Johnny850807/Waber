package tw.waterball.ddd.waber.springboot.user.chaos.eventbus;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.api.ChaosTriggeredException;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.ddd.commons.utils.DelayUtils;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect @Slf4j
@Configuration
public class EventBusPublishDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "user.EventBusPublishDelay";
    }

    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(1, 2, 7),
                areZeros(0, 5, 7, 11));
    }

    @Before("execution(* tw.waterball.ddd.events.EventBus.publish(..))")
    public void before(JoinPoint joinPoint) throws Throwable {

        if (isAlive()) {
            delay(3000);
        }
    }
}
