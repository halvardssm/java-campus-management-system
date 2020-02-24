package nl.tudelft.oopp.group39.building.Exceptions;

public class BuildingExistsException extends RuntimeException {
    public BuildingExistsException(int id) {
            super("Building with id " + id + " already exist.");
        }
}