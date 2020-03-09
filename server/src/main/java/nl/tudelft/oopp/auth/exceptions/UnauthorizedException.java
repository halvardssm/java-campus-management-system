package nl.tudelft.oopp.auth.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Wrong username or password");
    }
}
