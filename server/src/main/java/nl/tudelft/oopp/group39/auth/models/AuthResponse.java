package nl.tudelft.oopp.group39.auth.models;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class AuthResponse implements Serializable {
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
