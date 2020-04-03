package nl.tudelft.oopp.group39.facility.exceptions;

public class FacilityNotFoundException extends RuntimeException {
    /**
     * Creates an exception if the facility is not found.
     *
     * @param id the facility that is not found
     */
    public FacilityNotFoundException(Long id) {
        super("Facility with id " + id + " wasn't found.");
    }
}
