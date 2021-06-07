package tw.waterball.ddd.waber.springboot.commons.aop;

import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.attr;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.currentSpan;
import static tw.waterball.ddd.commons.utils.OpenTelemetryUtils.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.commons.utils.OpenTelemetryUtils.Attr;
import tw.waterball.ddd.events.Event;
import tw.waterball.ddd.events.EventBus;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Aspect
@Component
public class EventBusLoggingAspect {
    private final Logger log = LoggerFactory.getLogger(EventBus.class);

    @Before("execution(* tw.waterball.ddd.events.EventBus.publish(tw.waterball.ddd.events.Event))")
    public void before(JoinPoint joinPoint) {
        Event event = (Event) joinPoint.getArgs()[0];

        if (!(event instanceof UserLocationUpdatedEvent)) {
            currentSpan(event(event.getName()),
                    event.toMap().entrySet().stream()
                            .map((entry) -> attr(entry.getKey(), entry.getValue()))
                            .toArray(Attr[]::new))
                    .asLog(log::info);
        }
    }
}
