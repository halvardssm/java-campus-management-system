package nl.tudelft.oopp.group39.building.exceptions;

public class BuildingNotFoundException extends RuntimeException {
    public BuildingNotFoundException(Long id) {
        super("Building with id " + id + " wasn't found.");
    }
}
