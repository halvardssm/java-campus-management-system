package nl.tudelft.oopp.demo.objects.room.Exceptions;

public class RoomNotFoundException extends Throwable {
    public RoomNotFoundException(int id) {
        super("Room with id " + id + "wasn't found.");
    }

}
