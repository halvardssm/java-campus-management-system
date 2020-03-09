package nl.tudelft.oopp.user.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String id) {
        super("User with username '" + id + "' already exist.");
    }
}
