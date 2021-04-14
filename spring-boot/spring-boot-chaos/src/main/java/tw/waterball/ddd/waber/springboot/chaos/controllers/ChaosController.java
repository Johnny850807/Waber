package tw.waterball.ddd.waber.springboot.chaos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tw.waterball.chaos.api.ChaosEngine;
import tw.waterball.chaos.api.ChaosMiskillingException;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@RequestMapping("/api/chaos")
@RestController
public class ChaosController {
    private final ChaosEngine chaosEngine;
    private final Date startTime = new Date();
    private final List<String> killed = new ArrayList<>();
    private int miss = 0;

    public ChaosController(ChaosEngine chaosEngine) {
        this.chaosEngine = chaosEngine;
    }

    @GetMapping
    public InfoView ok() {
        return getInfo();
    }

    @GetMapping( "/kill/{chaosNumber}")
    public ResponseEntity<?> kill(@PathVariable int chaosNumber) {
        String chaosName = generateNumberToChaosMap().get(chaosNumber);
        if (chaosName == null) {
            return ResponseEntity.badRequest().body("Number " + chaosNumber + " doesn't exist.");
        }
        chaosEngine.kill(generateNumberToChaosMap().get(chaosNumber));
        killed.add(chaosName);
        return ResponseEntity.ok(getInfo());
    }

    public InfoView getInfo() {
        return new InfoView(miss, startTime, killed, generateNumberToChaosMap());
    }

    private Map<Integer, String> generateNumberToChaosMap() {
        var list = new ArrayList<>(chaosEngine.getChaosCollection());
        return new LinkedHashMap<>() {{
            IntStream.range(0, list.size())
                    .mapToObj(i -> Map.entry(i, list.get(i)))
                    .forEach(e -> put(e.getKey(), e.getValue().getName()));
        }};
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ChaosMiskillingException.class})
    public ResponseEntity<String> handleException(ChaosMiskillingException err) {
        miss ++;
        return ResponseEntity.badRequest().body(err.getMessage());
    }

}
