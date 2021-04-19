package tw.waterball.ddd.waber.springboot.match.chaos;

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
public class StartMatchingAPIBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "match.StartMatchingAPIBlocked";
    }

    @Override
    protected Criteria criteria() {
        return or(areZeros(3, 12, 14), areZeros(1, 2, 3, 4), negativeNumberAtPositions(3, 6, 15));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.match.controllers.MatchController.startMatching(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}
