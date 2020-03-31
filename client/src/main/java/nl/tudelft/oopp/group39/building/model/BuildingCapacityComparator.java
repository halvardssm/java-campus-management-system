package nl.tudelft.oopp.group39.building.model;

import java.util.Comparator;

public class BuildingCapacityComparator implements Comparator<Building> {
    @Override
    public int compare(Building o1, Building o2) {
        return Integer.compare(o1.getMaxCapacity(), o2.getMaxCapacity());
    }
}
