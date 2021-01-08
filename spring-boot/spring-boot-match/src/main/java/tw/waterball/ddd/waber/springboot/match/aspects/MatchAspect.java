package tw.waterball.ddd.waber.springboot.match.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.model.match.Match;
import tw.waterball.ddd.model.user.Driver;
import tw.waterball.ddd.model.user.Passenger;

import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
@Aspect
@Component
public class MatchAspect {
    private static final Logger logger = LogManager.getLogger();

    @Before("execution(* tw.waterball.ddd.model.match.Match.perform(..))")
    public void logRequest(JoinPoint joinPoint) {
        Match match = (Match) joinPoint.getThis();
        Passenger passenger = match.getPassenger();
        logger.info("Start matching(id={}): Passenger {} : ",
                match.getId(), passenger.getName());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @AfterReturning(value = "execution(* tw.waterball.ddd.model.match.Match.perform(..))")
    public void logResponse(JoinPoint joinPoint) {
        Match match = (Match) joinPoint.getThis();
        Passenger passenger = match.getPassenger();
        Optional<Driver> driverOptional = match.getDriverOptional();
        if (match.isCompleted()) {
            logger.info("Successfully Completed Match(id={}): Passenger {} --> Driver {} : ",
                    match.getId(), passenger.getName(), driverOptional.get().getName());
        } else {
            logger.info("Temporarily Pending Match(id={}): Passenger {} --> Driver {} : ",
                    match.getId(), passenger.getName(), driverOptional.get().getName());
        }
    }
}