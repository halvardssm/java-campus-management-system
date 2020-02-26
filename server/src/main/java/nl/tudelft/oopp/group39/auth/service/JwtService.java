package nl.tudelft.oopp.group39.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public static Integer tokenExpirationTime = 1000 * 3600;

    @Value("${jwt.token.secret}")
    private String secret;

    public String decryptUsername(String jwt) {
        return decrypt(jwt, Claims::getSubject);
    }

    public Date decryptExpiration(String jwt) {
        return decrypt(jwt, Claims::getExpiration);
    }

    private Boolean isExpired(String jwt) {
        return decryptExpiration(jwt).before(new Date());
    }

    public Boolean validate(String jwt, UserDetails userDetails) {
        String username = decryptUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isExpired(jwt);
    }

    private Key getSigningKey() {
        byte[] secretBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public String encrypt(UserDetails userDetails) {
        Date now = new Date();

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenExpirationTime))
            .signWith(getSigningKey())
            .compact();
    }

    public <T> T decrypt(String jwt, Function<Claims, T> resolver) {
        Claims claims = decryptAll(jwt);
        return resolver.apply(claims);
    }

    private Claims decryptAll(String jwt) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    }
}
