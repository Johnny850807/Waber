package tw.waterball.ddd.waber.springboot.match.chaos;

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
@Aspect @Slf4j
@Component
public class PerformMatchBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "match.PerformMatchBlocked";
    }

    @Override
    protected Criteria criteria() {
        return positiveNumberAtPositions(1, 2, 3, 4, 5, 6, 7);
    }

    @Before("execution(* tw.waterball.ddd.waber.match.domain.PerformMatch.execute(..))")
    public void before(JoinPoint joinPoint) {
        log.trace("Chaos CUT");
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}
