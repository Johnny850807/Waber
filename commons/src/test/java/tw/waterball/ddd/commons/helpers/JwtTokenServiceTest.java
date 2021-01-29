package tw.waterball.ddd.commons.helpers;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenServiceTest {

    public static final String SECRET = "████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████";

    @Test
    public void testJwtCreateAndParseCorrectlyAndShouldSucceed() {
        JwtTokenService jwtTokenService = new JwtTokenService(SECRET, TimeUnit.HOURS.toMillis(1));
        String token = jwtTokenService.createToken(150);
        String id = jwtTokenService.validateAndParseId(token);
        assertEquals("150", id);
    }

    @Test
    public void testTokenExpireShouldThrowTokenInvalidException() {
        JwtTokenService jwtTokenService = new JwtTokenService(SECRET, 0);
        String token = jwtTokenService.createToken(150);
        assertThrows(JwtTokenService.TokenInvalidException.class, () -> jwtTokenService.validateAndParseId(token));
    }

}