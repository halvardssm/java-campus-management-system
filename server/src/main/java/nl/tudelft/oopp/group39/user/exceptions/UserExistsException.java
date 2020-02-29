package nl.tudelft.oopp.group39.user.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String id) {
        super("User with username '" + id + "' already exist.");
    }
}
