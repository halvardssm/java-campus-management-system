package nl.tudelft.oopp.group39.config.exceptions;

import nl.tudelft.oopp.group39.config.Utils;

public class NotFoundException extends RuntimeException {
    /**
     * Creates an exception if the room to be found was not found.
     *
     * @param id the id of the room that was not found
     */
    public NotFoundException(String name, Object id) {
        super(Utils.firstLetterToUppercase(name) + " with id '" + id + "' wasn't found.");
    }

}
