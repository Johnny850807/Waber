package tw.waterball.ddd.waber.springboot.user.chaos.jpa;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.api.ChaosTriggeredException;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.ddd.commons.utils.DelayUtils;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect
@Component
public class SaveUserDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "user.CantSaveUser";
    }

    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(12, 13), and(areNotZeros(1, 2), positiveNumberAtPositions(5)));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.user.repositories.jpa.SpringBootUserRepository.save(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            delay(6000);
        }
    }
}
