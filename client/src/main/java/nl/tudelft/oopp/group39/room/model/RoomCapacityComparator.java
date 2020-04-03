package nl.tudelft.oopp.group39.room.model;

import java.util.Comparator;

public class RoomCapacityComparator implements Comparator<Room> {
    /**
     * Compares the capacity of two rooms.
     *
     * @param o1 the first room
     * @param o2 the second room
     * @return a negative integer if the first room has a smaller capacity
     *         zero if they have equal capacity
     *         a positive integer if the first room has a bigger capacity
     */
    @Override
    public int compare(Room o1, Room o2) {
        return o1.getCapacity().compareTo(o2.getCapacity());
    }
}
