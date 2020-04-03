package nl.tudelft.oopp.group39.building.model;

import java.util.Comparator;

public class BuildingCapacityComparator implements Comparator<Building> {
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
