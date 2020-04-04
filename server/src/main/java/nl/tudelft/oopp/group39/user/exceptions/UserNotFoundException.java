package nl.tudelft.oopp.group39.user.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    /**
     * Creates an exception if a username already exists.
     *
     * @param id the id of the user from the account that already exists
     */
    public UserNotFoundException(String id) {
        super("User", id);
    }
}
