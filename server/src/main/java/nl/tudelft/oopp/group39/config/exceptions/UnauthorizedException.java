package nl.tudelft.oopp.group39.config.exceptions;

public class UnauthorizedException extends RuntimeException {
    public static final String UNAUTHORIZED = "Wrong username or password";

    /**
     * Creates an exception.
     */
    public UnauthorizedException() {
        super(UNAUTHORIZED);
    }
}
