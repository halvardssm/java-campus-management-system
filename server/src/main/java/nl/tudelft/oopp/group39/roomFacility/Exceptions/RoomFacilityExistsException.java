package nl.tudelft.oopp.group39.roomFacility.Exceptions;

public class RoomFacilityExistsException extends RuntimeException {
    public RoomFacilityExistsException(int id) {
        super("RoomFacility with id " + id + " already exist.");
    }
}
