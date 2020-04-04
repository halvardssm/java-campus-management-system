package nl.tudelft.oopp.group39.room.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class RoomNotFoundException extends NotFoundException {
    /**
     * Creates an exception if the room to be found was not found.
     *
     * @param id the id of the room that was not found
     */
    public RoomNotFoundException(Long id) {
        super("Room", id);
    }

}
