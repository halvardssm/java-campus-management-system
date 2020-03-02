package nl.tudelft.oopp.group39.communication;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private String jwt;

    public JwtResponse() {
    }

    public JwtResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
