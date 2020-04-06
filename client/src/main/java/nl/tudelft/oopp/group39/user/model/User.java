package nl.tudelft.oopp.group39.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.sql.Blob;

@JsonIgnoreProperties(value = { "events"})
public class User {
    private String username;
    private String email;
    private String password;
    private Blob image;
    private String role;
    private ArrayNode bookings;

    /**
     * Creates a user.
     */
    public User() {
    }

    /**
     * Creates a user.
     *
     * @param username netid/username of the user
     * @param email    email of the user
     * @param password password of the user
     * @param image    image of the user
     * @param role     role of the user
     */
    public User(String username, String email, String password, Blob image, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role = role;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the image of the user.
     *
     * @return the image of the user
     */
    public Blob getImage() {
        return image;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the bookings of the user.
     *
     * @return the bookings of the user.
     */
    public ArrayNode getBookings() {
        return bookings;
    }
}
