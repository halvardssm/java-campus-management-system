package nl.tudelft.oopp.group39.auth.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Wrong username or password");
    }
}
