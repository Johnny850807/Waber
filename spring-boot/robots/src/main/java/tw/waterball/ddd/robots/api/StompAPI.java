package tw.waterball.ddd.robots.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import tw.waterball.ddd.events.MatchCompleteEvent;
import tw.waterball.ddd.events.UserLocationUpdatedEvent;
import tw.waterball.ddd.waber.springboot.commons.WaberProperties;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Component
public class StompAPI {
    private final Map<String, List<Subscriber>> subscriberMap = new HashMap<>();
    private final String brokerURL;
    private StompSession stompSession;
    private final StompSessionHandler stompSessionHandler = this.new StompSessionHandler();
    private final ObjectMapper objectMapper;

    public StompAPI(WaberProperties waberProperties,
                    ObjectMapper objectMapper) {
        this.brokerURL = waberProperties.getClient().getBrokerService().withPath("/broker");
        this.objectMapper = objectMapper;
    }

    public void start() throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MessageConverter() {
            @Override
            public Object fromMessage(Message<?> message, Class<?> aClass) {
                return message.getPayload();
            }

            @Override
            public Message<?> toMessage(Object o, MessageHeaders messageHeaders) {
                return new GenericMessage<>(String.valueOf(o));
            }
        });

        stompSession = stompClient.connect(brokerURL, stompSessionHandler).get();
    }

    private class StompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            log.info("StompSession connected.");
            StompAPI.this.stompSession = stompSession;
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
            log.error("Exception occurs.", throwable);
        }

        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
            log.error("Transport occurs.", throwable);
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object payload) {
            String content = new String((byte[]) payload);
            processMessageContent(stompHeaders.getDestination(), content);
        }

        @SneakyThrows
        private void processMessageContent(String destination, String content) {
            List<Subscriber> subscribersToBeRemoved = new ArrayList<>();
            var subscribers = subscriberMap.get(destination);
            for (Subscriber subscriber : subscribers) {
                Object event = convertToEvent(content);
                subscriber.onEvent(event, () -> subscribersToBeRemoved.add(subscriber));
            }
            subscribers.removeAll(subscribersToBeRemoved);
        }

        @SneakyThrows
        private Object convertToEvent(String content) {
            try {
                // try parse with json
                JsonNode jsonNode = objectMapper.readTree(content.getBytes(StandardCharsets.UTF_8));
                String name = jsonNode.get("name").asText();
                switch (name) {
                    case MatchCompleteEvent.NAME:
                        return objectMapper.readValue(content, MatchCompleteEvent.class);
                    case UserLocationUpdatedEvent.NAME:
                        return objectMapper.readValue(content, UserLocationUpdatedEvent.class);
                }
            } catch (JsonProcessingException ignored) {
            }
            return content;
        }
    }

    public void subscribe(Subscription subscription, Subscriber subscriber) {
        String topic = subscription.getTopic();
        subscriberMap.computeIfAbsent(topic, key -> new ArrayList<>())
                .add(subscriber);
        log.info("Subscribe --> {}", topic);
        stompSession.subscribe(topic, stompSessionHandler);
    }

    public interface Subscriber {
        void onEvent(Object event, Unsubscribe unsubscribe);
    }

    public interface Unsubscribe {
        void unsubscribe();
    }

    public static abstract class Subscription {
        abstract String getTopic();
    }

    @AllArgsConstructor
    public static class MatchedSubscription extends Subscription {
        private final int userId;

        @Override
        String getTopic() {
            return String.format("/topic/users/%d/matches", userId);
        }
    }

    @AllArgsConstructor
    public static class TripStateChangedSubscription extends Subscription {
        private final int userId;

        @Override
        String getTopic() {
            return String.format("/topic/users/%d/trips/current/state", userId);
        }
    }

    @AllArgsConstructor
    public static class UserLocationChangedSubscription extends Subscription {
        private final int userId;

        @Override
        String getTopic() {
            return String.format("/topic/users/%d/location", userId);
        }
    }

}
