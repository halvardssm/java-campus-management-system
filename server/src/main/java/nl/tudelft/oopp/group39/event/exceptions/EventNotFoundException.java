package nl.tudelft.oopp.group39.event.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event with id " + id + " wasn't found.");
    }
}