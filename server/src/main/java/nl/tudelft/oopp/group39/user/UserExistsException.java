package nl.tudelft.oopp.group39.user;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String id) {
        super("User with netid '" + id + "' already exist.");
    }
}
