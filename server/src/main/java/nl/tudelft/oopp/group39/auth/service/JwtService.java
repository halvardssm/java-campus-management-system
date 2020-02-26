package nl.tudelft.oopp.group39.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("jwt.secret")
    private String secretKey;

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

    public String encrypt(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 1000 * 3600))
            .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public <T> T decrypt(String jwt, Function<Claims, T> resolver) {
        Claims claims = decryptAll(jwt);
        return resolver.apply(claims);
    }

    private Claims decryptAll(String jwt) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody();
    }
}
