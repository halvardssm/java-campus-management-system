package nl.tudelft.oopp.group39.booking.exceptions;

public class BookingNotFoundException extends RuntimeException {
    /**
     * Creates an exception if the booking is not found.
     *
     * @param id the id of the booking that is not found
     */
    public BookingNotFoundException(Long id) {
        super("Booking with id " + id + " wasn't found.");
    }
}
