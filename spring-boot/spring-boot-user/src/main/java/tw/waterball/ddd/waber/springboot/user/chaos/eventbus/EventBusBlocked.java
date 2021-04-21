package tw.waterball.ddd.waber.springboot.user.chaos.eventbus;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import tw.waterball.chaos.annotations.ChaosEngineering;
import tw.waterball.chaos.core.md5.Md5Chaos;
import tw.waterball.ddd.events.EventBus;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@ChaosEngineering
@Aspect
@Configuration
public class EventBusBlocked extends Md5Chaos {
    @Override
    public String getName() {
        return "user.EventBusBlocked";
    }

    @Override
    protected Criteria criteria() {
        return and(positiveNumberAtPositions(1, 5, 8, 12), areZeros(13, 14));
    }

    @Around("execution(* tw.waterball.ddd.waber.springboot.user.config.RabbitEventBusConfiguration.rabbitEventBusSubscriber(..))")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        EventBus.Subscriber subscriber = (EventBus.Subscriber) joinPoint.proceed();
        return (EventBus.Subscriber) event -> {
            if (!isAlive()) {
                subscriber.onEvent(event);
            }
        };
    }
}