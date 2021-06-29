package tw.waterball.ddd.robots.api;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface BrokerAPI {
    void start() throws Exception;

    void subscribe(Subscription subscription, Subscriber subscriber);

    interface Subscriber {
        void onEvent(Object event, BrokerAPI.Unsubscribe unsubscribe);
    }

    interface Unsubscribe {
        void unsubscribe();
    }

    abstract class Subscription {
        abstract int getUserId();

        @Override
        public boolean equals(Object obj) {
            return getClass() == obj.getClass() && this.getUserId() == ((Subscription) obj).getUserId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUserId());
        }

        @Override
        public String toString() {
            return String.format("%s %d", getClass().getSimpleName(), getUserId());
        }
    }

    @AllArgsConstructor
    class MatchedSubscription extends BrokerAPI.Subscription {
        private final int userId;

        @Override
        public int getUserId() {
            return userId;
        }
    }

    @AllArgsConstructor
    class TripStateChangedSubscription extends BrokerAPI.Subscription {
        private final int userId;

        @Override
        public int getUserId() {
            return userId;
        }
    }

    @AllArgsConstructor
    class UserLocationChangedSubscription extends BrokerAPI.Subscription {
        private final int userId;

        @Override
        public int getUserId() {
            return userId;
        }
    }

}
