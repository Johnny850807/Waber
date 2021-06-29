package tw.waterball.ddd.waber.springboot.match.chaos.jpa;

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
public class SaveMatchDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "match.SaveMatchDelay";
    }

    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(5, 13), negativeNumberAtPositions(15));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.match.repositories.jpa.SpringBootMatchRepository.save*c(..))")
    public void before(JoinPoint joinPoint) {

        if (isAlive()) {
            delay(6000);
        }
    }
}
