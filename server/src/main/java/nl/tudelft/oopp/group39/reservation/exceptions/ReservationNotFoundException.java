package nl.tudelft.oopp.group39.reservation.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super("Reservation with id " + id + " wasn't found.");
    }
}