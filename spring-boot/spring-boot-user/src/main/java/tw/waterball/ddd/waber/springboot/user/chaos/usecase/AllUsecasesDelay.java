package tw.waterball.ddd.waber.springboot.user.chaos.usecase;

import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.core.md5.Md5Chaos;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect @Slf4j
@Configuration
public class AllUsecasesDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "user.AllUsecasesDelay";
    }

    @Override
    protected Criteria criteria() {
        return or(and(positiveNumberAtPositions(0, 1), negativeNumberAtPositions(13)),
                and(areZeros(2, 4, 6, 8, 10), positiveNumberAtPositions(15)));
    }

    @Before("execution(* tw.waterball.ddd.waber.*.usecases..execute(..))")
    public void before(JoinPoint joinPoint) throws Throwable {

        if (isAlive()) {
            delay(2000);
        }
    }
}
