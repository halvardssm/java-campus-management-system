package nl.tudelft.oopp.group39.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    public static final Integer TOKEN_EXPIRATION_TIME = 1000 * 3600;

    @Value("${auth.token.secret}")
    private String secret;

    /**
     * Decrypts the username.
     *
     * @param jwt The JWT string
     * @return The username
     */
    public String decryptUsername(String jwt) {
        return decrypt(jwt, Claims::getSubject);
    }

    /**
     * Decrypts expiration property.
     *
     * @param jwt The JWT string
     * @return The expiration date
     */
    public Date decryptExpiration(String jwt) {
        return decrypt(jwt, Claims::getExpiration);
    }

    /**
     * Checks if the JWT is expired.
     *
     * @param jwt The JWT string
     * @return If the JWT is expired
     */
    private Boolean isExpired(String jwt) {
        return decryptExpiration(jwt).before(new Date());
    }

    /**
     * Validates a JWT string.
     *
     * @param jwt         The JWT string to validate
     * @param userDetails The user to validate against
     * @return If the JWT is valid
     */
    public Boolean validate(String jwt, UserDetails userDetails) {
        String username = decryptUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isExpired(jwt);
    }

    /**
     * Creates a signing key from the secret.
     *
     * @return The signing key
     */
    private Key getSigningKey() {
        byte[] secretBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * Encrypts the user and generates a JWT string.
     *
     * @param userDetails The user to generate the JWT from
     * @return The JWT string
     */
    public String encrypt(UserDetails userDetails) {
        Date now = new Date();

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION_TIME))
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * Decrypts a claim of a JWT.
     *
     * @param jwt      The JWT string
     * @param resolver The claim to decrypt
     * @param <T>      The type of the decrypted claim
     * @return The decrypted claim
     */
    public <T> T decrypt(String jwt, Function<Claims, T> resolver) {
        Claims claims = decryptAll(jwt);
        return resolver.apply(claims);
    }

    /**
     * Decrypts all properties of the JWT.
     *
     * @param jwt The JWT string
     * @return All claims of a JWT
     */
    private Claims decryptAll(String jwt) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    }
}
