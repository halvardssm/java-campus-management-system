package nl.tudelft.oopp.group39.room.exceptions;

public class RoomExistsException extends RuntimeException {
    /**
     * Creates an exception if the room already exists.
     *
     * @param id the id of the room that already exists
     */
    public RoomExistsException(Long id) {
        super("Room with id " + id + " already exist.");
    }
}
