package nl.tudelft.oopp.group39.config.exceptions;

import nl.tudelft.oopp.group39.config.Utils;

public class NotNullException extends NullPointerException {

    /**
     * Creates an exception when something is null.
     *
     * @param name the name that cannot be null
     */
    public NotNullException(String name) {
        super(Utils.firstLetterToUppercase(name) + " can not be null.");
    }
}
