package nl.tudelft.oopp.group39.room.comparator;

import java.util.Comparator;
import nl.tudelft.oopp.group39.room.model.Room;

public class RoomCapacityComparator implements Comparator<Room> {

    @Override
    public int compare(Room o1, Room o2) {
        return o1.getCapacity().compareTo(o2.getCapacity());
    }
}
