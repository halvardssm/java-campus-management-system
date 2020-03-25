package nl.tudelft.oopp.group39.auth.dto;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseDto implements Serializable {
    private String token;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
