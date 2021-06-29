package tw.waterball.ddd.waber.springboot.user.chaos.jpa;

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
public class UpdateUserLocationDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "user.UpdateUserLocationDelay";
    }

    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(0, 2, 5), negativeNumberAtPositions(12));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.user.repositories.jpa.SpringBootUserRepository.updateLatestLocation(..))")
    public void before(JoinPoint joinPoint) {

        if (isAlive()) {
            delay(3000);
        }
    }
}
