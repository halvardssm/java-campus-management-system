package nl.tudelft.oopp.room.exceptions;

public class RoomExistsException extends RuntimeException {
    public RoomExistsException(int id) {
        super("Room with id " + id + " already exist.");
    }
}
