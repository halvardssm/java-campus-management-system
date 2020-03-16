package nl.tudelft.oopp.group39.auth.entities;

import org.springframework.stereotype.Component;

import java.io.Serializable;

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
