package tw.waterball.ddd.commons.utils;

import io.opentelemetry.api.trace.Span;
import lombok.Value;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class OpenTelemetryUtils {
    public static void currentSpan(Configuration... configurations) {
        Span span = Span.current();

        for (Configuration c : configurations) {
            if (c instanceof Attr) {
                Attr attr = (Attr) c;
                span.setAttribute(attr.key, attr.val);
            } else if (c instanceof Event) {
                span.addEvent(((Event) c).eventName);
            }
        }
    }

    public static Attr attr(Object key, Object val) {
        return new Attr(key.toString(), val.toString());
    }

    public static Event event(Object name) {
        return new Event(name.toString());
    }

    public interface Configuration {
    }

    @Value
    public static class Attr implements Configuration {
        public String key, val;
    }

    @Value
    public static class Event implements Configuration {
        public String eventName;
    }
}
