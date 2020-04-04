package nl.tudelft.oopp.group39.booking.exceptions;

public class BookingExistsException extends RuntimeException {
    /**
     * Creates an exception if a booking already exists.s
     *
     * @param id the id of the booking that already exists
     */
    public BookingExistsException(Long id) {
        super("Booking with id " + id + " already exists.");
    }
}