package tw.waterball.ddd.commons.utils;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import io.opentelemetry.api.trace.Span;
import lombok.Value;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class OpenTelemetryUtils {
    public static AndThenLog currentSpan(Configuration... configurations) {
        Span span = Span.current();

        for (Configuration c : configurations) {
            if (c instanceof Attr) {
                Attr attr = (Attr) c;
                span.setAttribute(attr.key, attr.val);
            } else if (c instanceof Event) {
                span.addEvent(((Event) c).eventName);
            }
        }

        return new AndThenLog(configurations);
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

    public static class AndThenLog {
        private final String message;

        public AndThenLog(Configuration... configurations) {
            String events = stream(configurations).filter(c -> c instanceof Event)
                    .map(c -> ((Event) c).eventName).collect(joining(", "));
            String attributes = stream(configurations).filter(c -> c instanceof Attr)
                    .map(c -> (Attr) c)
                    .map(attr -> attr.key + "=" + attr.val).collect(joining(", "));
            message = String.format("%s%s", events.isBlank() ? "" : "events: [ " + events + " ] ",
                    attributes.isBlank() ? "" : "attributes: [ " + attributes + " ] ");
        }

        public void asLog(Consumer<String> messageConsumer) {
            messageConsumer.accept(message);
        }
    }
}
