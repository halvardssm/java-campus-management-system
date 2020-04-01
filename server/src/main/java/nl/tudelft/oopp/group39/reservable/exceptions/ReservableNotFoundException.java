package nl.tudelft.oopp.group39.reservable.exceptions;

public class ReservableNotFoundException extends RuntimeException {
    public ReservableNotFoundException(Long id) {
        super("Reservable with id " + id + " wasn't found.");
    }
}