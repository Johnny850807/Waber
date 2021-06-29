package tw.waterball.ddd.waber.springboot.match.chaos;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.core.md5.Md5Chaos;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect @Slf4j
@Component
public class GetCurrentMatchApiDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "match.GetCurrentMatchApiDelay";
    }

    @Override
    protected Criteria criteria() {
        return or(negativeNumberAtPositions(2, 4, 10), positiveNumberAtPositions(1, 6, 7));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.match.controllers.MatchController.getUserCurrentMatch(..))")
    public void before(JoinPoint joinPoint) {

        if (isAlive()) {
            delay(6000);
        }
    }
}
