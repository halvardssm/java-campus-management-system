package nl.tudelft.oopp.group39.room.exceptions;

public class RoomNotFoundException extends RuntimeException {
    /**
     * Creates an exception if the room to be found was not found.
     *
     * @param id the id of the room that was not found
     */
    public RoomNotFoundException(Long id) {
        super("Room with id " + id + " wasn't found.");
    }

}
