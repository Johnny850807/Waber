package tw.waterball.ddd.waber.springboot.trip.chaos;

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
public class SaveTripDelay extends Md5Chaos {
    @Override
    public String getName() {
        return "trip.SaveTripDelay";
    }

    @Override
    protected Criteria criteria() {
        return and(areZeros(7, 8, 9), positiveNumberAtPositions(0, 1));
    }

    @Before("execution(* tw.waterball.ddd.waber.springboot.trip.repositories.jpa.SpringBootTripRepository.saveTrip*(..))")
    public void before(JoinPoint joinPoint) {

        if (isAlive()) {
            delay(6000);
        }
    }
}
