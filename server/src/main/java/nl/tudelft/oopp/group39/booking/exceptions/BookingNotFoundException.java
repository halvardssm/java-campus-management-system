package nl.tudelft.oopp.group39.booking.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class BookingNotFoundException extends NotFoundException {
    /**
     * Creates an exception if the booking is not found.
     *
     * @param id the id of the booking that is not found
     */
    public BookingNotFoundException(Long id) {
        super("Booking", id);
    }
}
