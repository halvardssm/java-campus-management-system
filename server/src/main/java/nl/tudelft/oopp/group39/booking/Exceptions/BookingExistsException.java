package nl.tudelft.oopp.group39.booking.Exceptions;

public class BookingExistsException extends RuntimeException {
    public BookingExistsException(int id) {
        super("Booking with id " + id + " already exists.");
    }
}