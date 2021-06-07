package tw.waterball.ddd.commons.utils;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;

import io.jsonwebtoken.lang.Strings;
import io.opentelemetry.api.trace.Span;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class OpenTelemetryUtils {
    public static AndThenLog currentSpan(Event event, Attr[] attrs) {
        Configuration[] configurations = new Configuration[attrs.length + 1];
        configurations[0] = event;
        System.arraycopy(attrs, 0, configurations, 1, attrs.length);
        return currentSpan(configurations);
    }

    public static AndThenLog currentSpan(Configuration... configurations) {
        Span span = Span.current();

        for (Configuration c : configurations) {
            c.instrument(span);
        }

        return new AndThenLog(configurations);
    }

    public static Attr attr(Object key, Object val) {
        return new Attr(String.valueOf(key), String.valueOf(val));
    }

    public static API api(String apiName) {
        return new API(apiName);
    }

    public static Event event(Object name) {
        return new Event(Strings.capitalize(String.valueOf(name)));
    }

    public interface Configuration {
        int priority();
        void instrument(Span span);
    }

    public static class API extends Event {
        public String apiName;

        public API(String apiName) {
            super(apiName);
            this.apiName = apiName;
        }

        @Override
        public void instrument(Span span) {
            span.setAttribute("api", apiName);
        }

        @Override
        public int priority() {
            return 1;
        }
    }

    @AllArgsConstructor
    public static class Attr implements Configuration {
        public String key, val;

        @Override
        public int priority() {
            return 100;
        }

        @Override
        public void instrument(Span span) {
            span.setAttribute(key, val);
        }

        @Override
        public String toString() {
            return String.format("%s=%s", key, val);
        }
    }

    public static class Event extends Attr {
        public String eventName;

        public Event(String eventName) {
            super("event", eventName);
            this.eventName = eventName;
        }

        @Override
        public int priority() {
            return 10;
        }

        @Override
        public void instrument(Span span) {
            span.addEvent(eventName);
        }
    }

    public static class AndThenLog {
        private final String message;

        public AndThenLog(Configuration... configurations) {
            message = stream(configurations)
                    .sorted(comparingInt(Configuration::priority))
                    .map(String::valueOf).collect(joining(" "));
        }

        public void asLog(Consumer<String> messageConsumer) {
            messageConsumer.accept(message);
        }
    }
}
