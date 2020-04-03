package nl.tudelft.oopp.group39.facility.exceptions;

public class FacilityExistsException extends RuntimeException {
    /**
     * Creates an exception if a facility already exists.
     *
     * @param id the id of the facility that already exists
     */
    public FacilityExistsException(Long id) {
        super("Facility with id " + id + " already exist.");
    }
}