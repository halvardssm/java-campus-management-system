package nl.tudelft.oopp.group39.reservation.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(Long id) {
        super("Reservation", id);
    }
}