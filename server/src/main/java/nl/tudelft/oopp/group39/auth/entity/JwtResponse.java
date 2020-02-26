package nl.tudelft.oopp.group39.auth.entity;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class JwtResponse implements Serializable {
    private String jwt;

    public JwtResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
