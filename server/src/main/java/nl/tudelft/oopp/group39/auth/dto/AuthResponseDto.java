package nl.tudelft.oopp.group39.auth.dto;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseDto implements Serializable {
    private String token;

    /**
     * Creates an authentication response.
     */
    public AuthResponseDto() {
    }

    /**
     * Creates an authentication response.
     *
     * @param token the login token
     */
    public AuthResponseDto(String token) {
        this.token = token;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }
}
