package tw.waterball.waber.chaos.springboot.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tw.waterball.waber.chaos.api.ChaosEngine;
import tw.waterball.waber.chaos.api.ChaosKillingFailException;
import tw.waterball.waber.chaos.tcp.TcpChaosServer;

import java.util.Date;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@AllArgsConstructor
@RequestMapping("/chaos")
@RestController
public class ChaosController {
    private final TcpChaosServer chaosServer;
    private final ChaosEngine chaosEngine;
    private final Date startTime = new Date();

    @GetMapping
    public InfoView ok() {
        return getInfo();
    }

    @PostMapping(value = "/kill", consumes = MediaType.TEXT_PLAIN_VALUE)
    public InfoView kill(@RequestBody String chaosName) {
        chaosEngine.kill(chaosName);
        return getInfo();
    }

    public InfoView getInfo() {
        return new InfoView(chaosEngine, startTime);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ChaosKillingFailException.class})
    public ResponseEntity<String> handleException(ChaosKillingFailException err) {
        return ResponseEntity.badRequest().body(err.getMessage());
    }

}
