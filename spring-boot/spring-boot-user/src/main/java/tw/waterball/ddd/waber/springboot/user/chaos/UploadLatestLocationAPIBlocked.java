package tw.waterball.ddd.waber.springboot.user.chaos;

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
public class UploadLatestLocationAPIBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "UploadLatestLocationAPIBlocked";
    }

    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(0, 2, 5), negativeNumberAtPositions(12));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.user.controllers.UserController.updateLatestLocation(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}
