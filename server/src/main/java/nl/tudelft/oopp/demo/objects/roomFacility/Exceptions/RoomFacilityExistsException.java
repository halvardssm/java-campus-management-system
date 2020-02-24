package nl.tudelft.oopp.demo.objects.roomFacility.Exceptions;

public class RoomFacilityExistsException extends RuntimeException {
    public RoomFacilityExistsException(int id) {
        super("RoomFacility with id " + id + " already exist.");
    }
}
