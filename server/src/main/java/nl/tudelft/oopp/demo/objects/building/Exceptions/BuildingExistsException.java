package nl.tudelft.oopp.demo.objects.building.Exceptions;

public class BuildingExistsException extends RuntimeException {
    public BuildingExistsException(int id) {
            super("Building with id " + id + " already exist.");
        }
}