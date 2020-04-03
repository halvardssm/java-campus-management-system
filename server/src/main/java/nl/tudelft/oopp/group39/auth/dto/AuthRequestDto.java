package nl.tudelft.oopp.group39.auth.dto;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public class AuthRequestDto implements Serializable {
    private String username;
    private String password;

    /**
     * Creates an authentication request.
     */
    public AuthRequestDto() {
    }

    /**
     * Creates an authentication request.
     *
     * @param username the username
     * @param password the password
     */
    public AuthRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Changes the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
