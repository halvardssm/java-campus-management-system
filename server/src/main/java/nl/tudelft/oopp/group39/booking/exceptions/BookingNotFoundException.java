package nl.tudelft.oopp.group39.booking.exceptions;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(long id) {
        super("Booking with id " + id + " wasn't found.");
    }

}
