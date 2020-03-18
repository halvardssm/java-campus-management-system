package nl.tudelft.oopp.group39.models;

import java.sql.Blob;

public class User {
    private String username;
    private String email;
    private String password;
    private Blob image;
    private String role;

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
}
