package nl.tudelft.oopp.group39.config.exceptions;

import nl.tudelft.oopp.group39.config.Utils;

public class NotNullException extends NullPointerException {

    public NotNullException(String name) {
        super(Utils.firstLetterToUppercase(name) + " can not be null.");
    }
}
