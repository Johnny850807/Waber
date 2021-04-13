package tw.waterball.ddd.waber.springboot.user.chaos;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tw.waterball.waber.chaos.core.ChaosTriggeredException;
import tw.waterball.waber.chaos.core.WaberChaos;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Aspect
@Component
public class UploadLatestLocationAPIChaos extends WaberChaos {
    @Override
    protected Criteria criteria() {
        return or(positiveNumberAtPositions(0, 5, 9, 15), negativeNumberAtPositions(12));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.user.controllers.UserController.updateLatestLocation(..))")
    public void before(JoinPoint joinPoint) {
        if (isAlive()) {
            throw new ChaosTriggeredException();
        }
    }
}