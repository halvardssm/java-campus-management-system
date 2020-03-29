package nl.tudelft.oopp.group39.building.exceptions;

public class BuildingExistsException extends RuntimeException {
    public BuildingExistsException(Long id) {
        super("Building with id " + id + " already exist.");
    }
}
