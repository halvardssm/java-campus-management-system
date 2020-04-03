package nl.tudelft.oopp.group39.reservation.exceptions;

public class CreationException extends RuntimeException {
    /**
     * Creates an exception if creating a reservation failed.
     */
    public CreationException() {
        super("Could not create reservation");
    }
}
