package nl.tudelft.oopp.facility.exceptions;

public class FacilityExistsException extends RuntimeException {
    public FacilityExistsException(int id) {
        super("Facility with id " + id + " already exist.");
    }
}