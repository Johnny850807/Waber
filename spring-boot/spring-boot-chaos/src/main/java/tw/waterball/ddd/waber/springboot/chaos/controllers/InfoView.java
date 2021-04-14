package tw.waterball.ddd.waber.springboot.chaos.controllers;

import static java.util.stream.Collectors.toList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.ChaosEngine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoView {
    public int miss;
    public List<Item> chaos;
    public List<String> killed;
    public int remaining;
    public long elapsedTime;

    public InfoView(int miss, Date startTime, List<String> killed, Map<Integer, String> numberToAliveChaosName) {
        this.miss = miss;
        this.killed = killed;
        this.chaos = numberToAliveChaosName.entrySet()
                .stream().map(e -> new Item(e.getValue(), new Item.Link("/api/chaos/kill/" + e.getKey())))
                .collect(Collectors.toUnmodifiableList());
        this.remaining = numberToAliveChaosName.size();
        this.elapsedTime = System.currentTimeMillis() - startTime.getTime();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        String name;
        Link _link;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Link {
            public String kill;
        }
    }
}
