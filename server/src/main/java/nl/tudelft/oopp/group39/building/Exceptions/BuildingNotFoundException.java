package nl.tudelft.oopp.group39.building.Exceptions;

public class BuildingNotFoundException extends RuntimeException {
    public BuildingNotFoundException(int id) {
        super("Room with id " + id + " wasn't found.");
    }

}
