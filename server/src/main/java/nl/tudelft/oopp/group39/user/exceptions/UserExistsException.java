package nl.tudelft.oopp.group39.user.exceptions;

public class UserExistsException extends RuntimeException {
    /**
     * Creates an exception if a username already exists.
     *
     * @param id the id of the user from the account that already exists
     */
    public UserExistsException(String id) {
        super("User with username '" + id + "' already exist.");
    }
}
