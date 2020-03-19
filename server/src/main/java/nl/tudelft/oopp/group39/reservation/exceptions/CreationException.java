package nl.tudelft.oopp.group39.reservation.exceptions;

public class CreationException extends RuntimeException {
    public CreationException() {
        super("Could not create reservation");
    }
}
