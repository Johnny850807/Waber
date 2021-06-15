package tw.waterball.ddd.waber.springboot.chaos.controllers;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tw.waterball.chaos.api.Chaos;
import tw.waterball.chaos.api.ChaosEngine;
import tw.waterball.chaos.api.ChaosMiskillingException;
import tw.waterball.chaos.api.FunValue;
import tw.waterball.chaos.core.md5.Md5FunValue;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@RequestMapping("/api/chaos")
@RestController
@RequiredArgsConstructor
public class ChaosController {
    private final ChaosEngine chaosEngine;
    private List<String> killed = new ArrayList<>();
    private Date startTime = new Date();
    private FunValue funValue;
    private int miss = 0;


    @GetMapping
    public InfoView ok() {
        return getInfo();
    }

    @GetMapping("/kill/all")
    public void reset(HttpServletResponse response) throws IOException {
        killed.addAll(chaosEngine.getAliveChaos().stream().map(Chaos::getName).collect(toList()));
        chaosEngine.killAll();
        response.sendRedirect("/api/chaos");
    }

    @GetMapping("/fun/replay")
    public void replay(HttpServletResponse response) throws IOException {
        playFunValue(UUID.randomUUID().toString(), response);
    }

    @GetMapping("/fun/{funValue}")
    public void playFunValue(@PathVariable String funValue, HttpServletResponse response) throws IOException {
        this.funValue = new Md5FunValue(funValue);
        chaosEngine.effect(this.funValue);
        killed = new ArrayList<>();
        startTime = new Date();
        miss = 0;
        response.sendRedirect("/api/chaos");
    }

    @GetMapping(value = "/kill/{chaosNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void kill(@PathVariable int chaosNumber, HttpServletResponse response) throws IOException {
        String chaosName = generateNumberToChaosMap().get(chaosNumber);
        if (chaosName == null) {
            response.setStatus(400);
            IOUtils.copy(new StringReader("Number " + chaosNumber + " doesn't exist."), response.getWriter());
        }
        chaosEngine.kill(generateNumberToChaosMap().get(chaosNumber));
        killed.add(chaosName);

        response.sendRedirect("/api/chaos");
    }

    public InfoView getInfo() {
        return new InfoView(funValue, miss, startTime, killed,
                chaosEngine.getAliveChaos().stream().map(Chaos::getName).collect(toList()),
                generateNumberToChaosMap());
    }

    private Map<Integer, String> generateNumberToChaosMap() {
        var list = new ArrayList<>(chaosEngine.getChaosCollection());
        return new LinkedHashMap<>() {{
            IntStream.range(0, list.size())
                    .mapToObj(i -> entry(i, list.get(i)))
                    .forEach(e -> put(e.getKey(), e.getValue().getName()));
        }};
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ChaosMiskillingException.class})
    public ResponseEntity<String> handleException(ChaosMiskillingException err) {
        miss++;
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body("{\"error\": \""+err.getMessage()+"\"}");
    }

}
