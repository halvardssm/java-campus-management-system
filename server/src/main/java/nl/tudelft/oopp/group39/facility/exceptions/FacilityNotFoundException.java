package nl.tudelft.oopp.group39.facility.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class FacilityNotFoundException extends NotFoundException {
    /**
     * Creates an exception if the facility is not found.
     *
     * @param id the facility that is not found
     */
    public FacilityNotFoundException(Long id) {
        super("Facility", id);
    }
}
