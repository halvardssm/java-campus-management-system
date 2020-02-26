package nl.tudelft.oopp.group39.auth.exception;

public class BadAuthException extends RuntimeException {
    public BadAuthException() {
        super("Wrong username or password");
    }
}
