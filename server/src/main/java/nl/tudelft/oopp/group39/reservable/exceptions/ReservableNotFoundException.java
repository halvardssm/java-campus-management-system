package nl.tudelft.oopp.group39.reservable.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class ReservableNotFoundException extends NotFoundException {
    public ReservableNotFoundException(Long id) {
        super("Reservable", id);
    }
}