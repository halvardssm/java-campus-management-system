package nl.tudelft.oopp.building.exceptions;

public class BuildingNotFoundException extends RuntimeException {
    public BuildingNotFoundException(int id) {
        super("Room with id " + id + " wasn't found.");
    }
}
