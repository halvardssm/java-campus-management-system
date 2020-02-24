package nl.tudelft.oopp.demo.objects.facility.Exceptions;

public class FacilityExistsException extends RuntimeException {
    public FacilityExistsException(int id) {
        super("Facility with id " + id + " already exist.");
    }
}