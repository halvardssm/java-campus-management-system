package nl.tudelft.oopp.group39.room.exceptions;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(int id) {
        super("Room with id " + id + " wasn't found.");
    }

}
