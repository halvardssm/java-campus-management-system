package nl.tudelft.oopp.facility.exceptions;

public class FacilityNotFoundException extends RuntimeException {
    public FacilityNotFoundException(int id) {
        super("Facility with id " + id + " wasn't found.");
    }

}
