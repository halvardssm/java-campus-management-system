package nl.tudelft.oopp.group39.config.exceptions;

import nl.tudelft.oopp.group39.config.Utils;

public class ExistsException extends RuntimeException {
    /**
     * Creates an exception if a username already exists.
     *
     * @param id the id of the user from the account that already exists
     */
    public ExistsException(String name, Object id) {
        super(Utils.firstLetterToUppercase(name) + " with id '" + id + "' already exist.");
    }
}
