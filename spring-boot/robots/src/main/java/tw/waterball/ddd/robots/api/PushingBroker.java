package tw.waterball.ddd.robots.api;


import static tw.waterball.ddd.commons.utils.DelayUtils.delay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;


/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Slf4j
@Component
public class PushingBroker implements BrokerAPI {
    private final Map<String, List<Subscriber>> subscriberMap = new HashMap<>();
    private final String brokerURL;
    private final ObjectMapper objectMapper;
    private WebSocketStompClient stompClient;
    private int currentSessionNumber = 0;
    private StompSession stompSession;
    private StompSession backupStompSession;
    private PushingBroker.StompSessionHandler backupSessionHandler;
    private PushingBroker.StompSessionHandler stompSessionHandler;

    public PushingBroker(WaberProperties waberProperties,
                         ObjectMapper objectMapper) {
        this.brokerURL = waberProperties.getClient().getBrokerService().withPath("/broker");
        this.objectMapper = objectMapper;
    }

    @Override
    public void start() throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);
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

        stompSession = stompClient.connect(brokerURL, stompSessionHandler = new StompSessionHandler(currentSessionNumber)).get();
        delay(1000);
        backupStompSession = stompClient.connect(brokerURL, backupSessionHandler = new StompSessionHandler(currentSessionNumber + 1)).get();
    }

    @AllArgsConstructor
    private class StompSessionHandler extends StompSessionHandlerAdapter {
        private final int handlerNumber;

        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            synchronized (PushingBroker.this) {
                log.info("StompSession connected.");
                PushingBroker.this.stompSession = stompSession;
                subscriberMap.keySet().forEach(topic -> stompSession.subscribe(topic, stompSessionHandler));
            }
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
            log.error("Exception occurs.", throwable);
        }

        @SneakyThrows
        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
            synchronized (PushingBroker.this) {
                log.error("Transport occurs.", throwable);
                PushingBroker.this.stompSession = backupStompSession;
                PushingBroker.this.stompSessionHandler = backupSessionHandler;
                currentSessionNumber++;
                backupStompSession = stompClient.connect(brokerURL, backupSessionHandler = new StompSessionHandler(currentSessionNumber + 1)).get();
            }
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object payload) {
            String content = new String((byte[]) payload);
            processMessageContent(stompHeaders.getDestination(), content);
        }

        @SneakyThrows
        private void processMessageContent(String destination, String content) {
            synchronized (PushingBroker.this) {
                Object event = convertToEvent(content);
                processEvent(new Event(destination, event));
            }
        }

        private void processEvent(Event e) {
            if (handlerNumber == currentSessionNumber) {
                broadcastEvent(e);
            }
        }

        private void broadcastEvent(Event e) {
            List<Subscriber> subscribersToBeRemoved = new ArrayList<>();
            var subscribers = subscriberMap.get(e.destination);

            for (Subscriber subscriber : subscribers) {
                subscriber.onEvent(e.event, () -> subscribersToBeRemoved.add(subscriber));
            }
            subscribers.removeAll(subscribersToBeRemoved);
        }
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


    @Override
    public synchronized void subscribe(Subscription subscription, Subscriber subscriber) {
        String topic = getTopic(subscription);
        subscriberMap.computeIfAbsent(topic, key -> new ArrayList<>())
                .add(subscriber);
        log.info("Subscribe --> {}", topic);
        stompSession.subscribe(topic, stompSessionHandler);
        backupStompSession.subscribe(topic, backupSessionHandler);
    }

    public String getTopic(Subscription subscription) {
        if (subscription instanceof MatchedSubscription) {
            return String.format("/topic/users/%d/matches", subscription.getUserId());
        }
        if (subscription instanceof TripStateChangedSubscription) {
            return String.format("/topic/users/%d/trips/current/state", subscription.getUserId());
        }
        if (subscription instanceof UserLocationChangedSubscription) {
            return String.format("/topic/users/%d/location", subscription.getUserId());
        }
        throw new IllegalStateException("Type unmatched.");
    }

    @Value
    private static class Event {
        String destination;
        Object event;
    }
}