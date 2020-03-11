package nl.tudelft.oopp.group39.auth.entities;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class AuthResponse implements Serializable {
    private String jwt;

    public AuthResponse() {
    }

    public AuthResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
