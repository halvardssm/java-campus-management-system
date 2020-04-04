package nl.tudelft.oopp.group39.building.exceptions;

import nl.tudelft.oopp.group39.config.abstracts.NotFoundException;

public class BuildingNotFoundException extends NotFoundException {
    /**
     * Creates an exception if the building is not found.
     *
     * @param id the id of the building that is not found
     */
    public BuildingNotFoundException(Long id) {
        super("Building", id);
    }
}
