package tw.watewrball.waber.springboot.chaos.controllers;

import static java.util.stream.Collectors.toList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tw.waterball.waber.chaos.api.Chaos;
import tw.waterball.waber.chaos.api.ChaosEngine;

import java.util.Date;
import java.util.List;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoView {
    public List<String> chaos;
    public int remaining;
    public long elapsedTime;

    public InfoView(ChaosEngine chaosEngine, Date startTime) {
        this.chaos = chaosEngine.getChaosCollection().stream().map(Chaos::getName).collect(toList());
        this.remaining = chaosEngine.getAliveChaos().size();
        this.elapsedTime = System.currentTimeMillis() - startTime.getTime();
    }

}
