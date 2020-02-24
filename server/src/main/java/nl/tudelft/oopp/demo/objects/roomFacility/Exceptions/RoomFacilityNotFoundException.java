package nl.tudelft.oopp.demo.objects.roomFacility.Exceptions;

public class RoomFacilityNotFoundException extends RuntimeException {
    public RoomFacilityNotFoundException(int id) {
        super("RoomFacility with id " + id + " wasn't found.");
    }

}
