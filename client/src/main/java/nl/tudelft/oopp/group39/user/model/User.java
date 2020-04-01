package nl.tudelft.oopp.group39.user.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import java.sql.Blob;

public class User {
    private String username;
    private String email;
    private String password;
    private Blob image;
    private String role;
    private ArrayNode bookings;


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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Blob getImage() {
        return image;
    }

    public String getRole() {
        return role;
    }

    public ArrayNode getBookings() {
        return bookings;
    }

}
