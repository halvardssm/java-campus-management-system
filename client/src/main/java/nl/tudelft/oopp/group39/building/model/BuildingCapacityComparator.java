package nl.tudelft.oopp.group39.building.model;

import java.util.Comparator;

public class BuildingCapacityComparator implements Comparator<Building> {
    /**
     * Compares the capacity of the buildings.
     * 
     * @param o1 the first building
     * @param o2 the second building
     * @return 0 if both rooms have size zero, 1 if the second room has size 0, and -1 if the first
     *         room has size 0
     */
    @Override
    public int compare(Building o1, Building o2) {
        if (o2.getRooms().size() == 0 & o1.getRooms().size() == 0) {
            return 0;
        }
        if (o2.getRooms().size() == 0) {
            return 1;
        }
        if (o1.getRooms().size() == 0) {
            return -1;
        }
        return Integer.compare(o1.getMaxCapacity(), o2.getMaxCapacity());
    }
}
