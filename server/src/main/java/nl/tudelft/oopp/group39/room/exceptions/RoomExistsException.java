package nl.tudelft.oopp.group39.room.exceptions;

public class RoomExistsException extends RuntimeException {
    public RoomExistsException(Long id) {
        super("Room with id " + id + " already exist.");
    }
}
