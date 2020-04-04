package nl.tudelft.oopp.group39.building.exceptions;

public class BuildingExistsException extends RuntimeException {
    /**
     * Creates an exception if the building already exists.
     *
     * @param id the id of the building that already exists
     */
    public BuildingExistsException(Long id) {
        super("Building with id " + id + " already exist.");
    }
}
