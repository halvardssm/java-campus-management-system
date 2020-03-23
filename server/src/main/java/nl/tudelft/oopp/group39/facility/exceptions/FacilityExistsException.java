package nl.tudelft.oopp.group39.facility.exceptions;

public class FacilityExistsException extends RuntimeException {
    public FacilityExistsException(int id) {
        super("Facility with id " + id + " already exist.");
    }
}