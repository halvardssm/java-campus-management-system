package nl.tudelft.oopp.group39.booking.exceptions;

public class BookingExistsException extends RuntimeException {
    public BookingExistsException(Long id) {
        super("Booking with id " + id + " already exists.");
    }
}