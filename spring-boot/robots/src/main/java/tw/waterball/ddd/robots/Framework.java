package tw.waterball.ddd.robots;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tw.waterball.ddd.api.match.MatchServiceDriver;
import tw.waterball.ddd.api.trip.TripServiceDriver;
import tw.waterball.ddd.model.user.Passenger;
import tw.waterball.ddd.robots.api.API;
import tw.waterball.ddd.robots.api.StompAPI;
import tw.waterball.ddd.robots.life.Life;
import tw.waterball.ddd.robots.life.PassengerBot;
import tw.waterball.ddd.waber.api.payment.UserServiceDriver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Component
public class Framework {
    public static final int INTERVAL = 3;
    private final Collection<Life> lives = new CopyOnWriteArrayList<>();
    private boolean running = false;
    private final List<LivesListener> livesListeners = new LinkedList<>();
    private final StompAPI stompAPI;

    public Framework(@Value("${maxDrivers}") int maxDrivers,
                     @Value("${maxPassengers}") int maxPassengers,
                     StompAPI stompAPI, UserServiceDriver userServiceDriver,
                     MatchServiceDriver matchServiceDriver,
                     TripServiceDriver tripServiceDriver) {
        this.stompAPI = stompAPI;
        lives.add(new UserGenerator(maxDrivers, maxPassengers, this, stompAPI,
                new API(userServiceDriver, matchServiceDriver, tripServiceDriver)));
    }

    @SneakyThrows
    public void start() {
        stompAPI.start();
        running = true;
        for (long i = 0; i < Long.MAX_VALUE && running; i++) {
            final long time = i;
            livesListeners.forEach(l -> l.onTick(lives));
            lives.forEach(life -> life.tick(time));
            Thread.sleep(INTERVAL);
        }
        running = false;
    }

    void addLife(Life life) {
        lives.add(life);
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public interface LivesListener {
        void onTick(Collection<Life> lives);
    }

    public void addLivesListener(LivesListener livesListener) {
        livesListeners.add(livesListener);
    }

    public PassengerBot getPassengerById(int passengerId) {
        for (Life life : lives) {
            if (life instanceof PassengerBot) {
                if (((PassengerBot) life).getPassengerId().orElse(-9999) == passengerId) {
                    return (PassengerBot) life;
                }
            }
        }
        throw new IllegalStateException("Can't find the passenger-bot.");
    }
}
