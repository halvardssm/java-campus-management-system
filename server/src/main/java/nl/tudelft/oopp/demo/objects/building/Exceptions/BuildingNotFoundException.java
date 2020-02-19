package nl.tudelft.oopp.demo.objects.building.Exceptions;

public class BuildingNotFoundException extends Throwable {
    public BuildingNotFoundException(int id) {
        super("Room with id " + id + "wasn't found.");
    }

}
