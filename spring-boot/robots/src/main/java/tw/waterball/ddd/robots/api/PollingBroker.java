//package tw.waterball.ddd.robots.api;
//
//import static tw.waterball.ddd.commons.utils.DelayUtils.delay;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import tw.waterball.ddd.events.MatchCompleteEvent;
//import tw.waterball.ddd.events.TripStateChangedEvent;
//import tw.waterball.ddd.events.UserLocationUpdatedEvent;
//import tw.waterball.ddd.model.geo.Location;
//import tw.waterball.ddd.model.trip.TripStateType;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CopyOnWriteArrayList;
//
///**
// * @author Waterball (johnny850807@gmail.com)
// */
//@Slf4j
//@Component
//public class PollingBroker implements BrokerAPI {
//    private final Map<Subscription, List<Subscriber>> subscriberMap = new HashMap<>();
//    private final Map<MatchedSubscription, Boolean> matchStatus = new HashMap<>();
//    private final Map<TripStateChangedSubscription, TripStateType> tripState = new HashMap<>();
//    private final Map<UserLocationChangedSubscription, Location> locationStatus = new HashMap<>();
//    private final API api;
//    private final List<Runnable> tasks = new CopyOnWriteArrayList<>();
//
//    public PollingBroker(API api) {
//        this.api = api;
//    }
//
//    @Override
//    public void start() {
//        new Thread(() -> {
//            while (true) {
//                tasks.parallelStream().forEach(Runnable::run);
//                delay(500);
//            }
//        }).start();
//    }
//
//    @Override
//    public void subscribe(BrokerAPI.Subscription subscription, Subscriber subscriber) {
//        log.info("Subscribe --> {}", subscription.getClass().getSimpleName());
//        subscriberMap.computeIfAbsent(subscription, key -> new ArrayList<>()).add(subscriber);
//        if (subscription instanceof BrokerAPI.MatchedSubscription) {
//            subscribeToMatchedSubscription((MatchedSubscription) subscription);
//        } else if (subscription instanceof BrokerAPI.TripStateChangedSubscription) {
//            subscribeToTripStateChangedSubscription((TripStateChangedSubscription) subscription);
//        } else if (subscription instanceof BrokerAPI.UserLocationChangedSubscription) {
//            subscribeToUserLocationChangedSubscription((UserLocationChangedSubscription) subscription);
//        } else {
//            throw new IllegalStateException("Type unmatched.");
//        }
//    }
//
//    private void subscribeToMatchedSubscription(MatchedSubscription subscription) {
//        tasks.add(() -> {
//            try {
//                var match = api.getCurrentMatch(subscription.getUserId());
//                if (match != null) {
//                    if (match.completed != matchStatus.getOrDefault(subscription, false)) {
//                        List<Subscriber> toBeRemoved = new ArrayList<>();
//                        matchStatus.put(subscription, match.completed);
//                        var subscribers = subscriberMap.get(subscription);
//                        for (Subscriber s : subscribers) {
//                            s.onEvent(new MatchCompleteEvent(match.toEntity()), () -> toBeRemoved.add(s));
//                        }
//                        subscribers.removeAll(toBeRemoved);
//                    }
//                }
//            } catch (Exception ignored) {
//                System.err.println(ignored.getMessage());
//            }
//        });
//    }
//
//    private void subscribeToTripStateChangedSubscription(TripStateChangedSubscription subscription) {
//        final Runnable task = () -> {
//            try {
//                var trip = api.getCurrentTrip(subscription.getUserId());
//                var state = tripState.get(subscription) == TripStateType.DRIVING ? TripStateType.ARRIVED : null;
//                if (state != null) {
//                    if (state != tripState.getOrDefault(subscription, null)) {
//                        List<Subscriber> toBeRemoved = new ArrayList<>();
//                        tripState.put(subscription, trip.state);
//                        var subscribers = subscriberMap.get(subscription);
//                        for (Subscriber s : subscribers) {
//                            s.onEvent(trip.state.toString(), () -> {
//                                toBeRemoved.add(s);
//                                tasks.remove(task);
//                            });
//                        }
//                        subscribers.removeAll(toBeRemoved);
//                    }
//                }
//            } catch (Exception ignored) {
//                ignored.printStackTrace();
//            }
//        });
//        tasks.add(task);
//    }
//
//    private void subscribeToUserLocationChangedSubscription(UserLocationChangedSubscription subscription) {
//        tasks.add(() -> {
//            try {
//                var user = api.getUser(subscription.getUserId());
//                if (user != null) {
//                    var location = user.getLocation();
//                    if (location.equals(locationStatus.getOrDefault(subscription, null))) {
//                        List<Subscriber> toBeRemoved = new ArrayList<>();
//                        locationStatus.put(subscription, location);
//                        var subscribers = subscriberMap.get(subscription);
//                        for (Subscriber s : subscribers) {
//                            s.onEvent(new UserLocationUpdatedEvent(subscription.getUserId(), location), () -> toBeRemoved.add(s));
//                        }
//                        subscribers.removeAll(toBeRemoved);
//                    }
//                }
//            } catch (Exception ignored) {
//                System.err.println(ignored.getMessage());
//            }
//        });
//    }
//
//}
