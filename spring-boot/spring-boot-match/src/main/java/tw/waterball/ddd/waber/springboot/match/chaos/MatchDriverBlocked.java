package tw.waterball.ddd.waber.springboot.match.chaos;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

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
public class MatchDriverBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "Match.MatchDriverBlocked";
    }

    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(1, 2, 3), positiveNumberAtPositions(12, 13, 14),
                negativeNumberAtPositions(1, 5, 12, 13), areNotZeros(5, 10, 15));
    }

    @Before("execution(* tw.waterball.ddd.model.match.Match.matchDriver(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}
