package tw.waterball.ddd.robots;

import tw.waterball.ddd.robots.api.API;
import tw.waterball.ddd.robots.api.StompAPI;
import tw.waterball.ddd.robots.life.DriverBot;
import tw.waterball.ddd.robots.life.PassengerBot;
import tw.waterball.ddd.robots.life.ScheduledLife;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class UserGenerator extends ScheduledLife {
    private static int id = 0;
    public final int MAX_DRIVER;
    public final int MAX_PASSENGERS;
    private final Framework framework;
    private final StompAPI stompAPI;
    private final List<DriverBot> driverBots = new LinkedList<>();
    private final List<PassengerBot> passengerBots = new LinkedList<>();
    private final API api;

    public UserGenerator(int maxDrivers, int maxPassengers, Framework framework, StompAPI stompAPI, API api) {
        this.MAX_DRIVER = maxDrivers;
        this.MAX_PASSENGERS = maxPassengers;
        this.framework = framework;
        this.stompAPI = stompAPI;
        this.api = api;
    }

    @Override
    protected void onNewBorn() {
        scheduleFixed(100, (schedule) -> {
            if (driverBots.size() < MAX_DRIVER) {
                DriverBot driverBot = new DriverBot("D-"+id++, stompAPI, api, framework);
                driverBots.add(driverBot);
                framework.addLife(driverBot);
            } else if (passengerBots.size() < MAX_PASSENGERS){
                PassengerBot passengerBot = new PassengerBot("P-"+id++, stompAPI, api, framework);
                passengerBots.add(passengerBot);
                framework.addLife(passengerBot);
            }
        });
    }

    @Override
    public String getName() {
        return "UserGenerator";
    }

}
