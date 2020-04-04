package nl.tudelft.oopp.group39.event.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(Long id) {
        super("Event", id);
    }
}