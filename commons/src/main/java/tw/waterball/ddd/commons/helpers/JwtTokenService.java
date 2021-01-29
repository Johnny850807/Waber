package tw.waterball.ddd.commons.helpers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.inject.Named;
import java.util.Date;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class JwtTokenService {
    private final SecretKey key;
    private final Date expiration;

    public JwtTokenService(String secret,
                           long expiration) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = new Date(expiration);
    }

    public String createToken(Object id) {
        return createToken(String.valueOf(id));
    }

    public String createToken(String id) {
        Date expirationDate = new Date((System.currentTimeMillis() + this.expiration.getTime()));
        return compactTokenString(expirationDate, id);
    }

    public String validateAndParseId(String token) throws TokenInvalidException {
        Jws<Claims> jwt;
        try {
            jwt = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(token);
        } catch (JwtException err) {
            throw new TokenInvalidException(err);
        }

        return jwt.getBody().getSubject();
    }

    private String compactTokenString(Date expirationDate, String id) {
        return Jwts.builder()
                .setSubject(id)
                .setExpiration(expirationDate)
                .signWith(key).compact();
    }

    public static class TokenInvalidException extends IllegalStateException {
        public TokenInvalidException(Throwable cause) {
            super(cause);
        }
    }

}
