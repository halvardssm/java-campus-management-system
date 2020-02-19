package nl.tudelft.oopp.demo.objects.room.Exceptions;

public class RoomExistsException extends RuntimeException {
    public RoomExistsException(int id) {
        super("Room with id " + id + "already exist.");
    }
}
