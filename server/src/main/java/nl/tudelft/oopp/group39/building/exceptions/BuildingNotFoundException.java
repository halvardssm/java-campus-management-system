package nl.tudelft.oopp.group39.building.exceptions;

public class BuildingNotFoundException extends RuntimeException {
    /**
     * Creates an exception if the building is not found.
     *
     * @param id the id of the building that is not found
     */
    public BuildingNotFoundException(Long id) {
        super("Building with id " + id + " wasn't found.");
    }
}
