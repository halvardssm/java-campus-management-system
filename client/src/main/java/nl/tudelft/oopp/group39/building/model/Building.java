package nl.tudelft.oopp.group39.building.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.room.model.Room;

import nl.tudelft.oopp.group39.room.model.RoomCapacityComparator;

public class Building {

    private Long id;

    private String name;

    private String location;

    private String description;

    private String open;

    private String closed;

    private Set<Room> rooms = new HashSet<>();

    public Building() {

    }

    /**
     * Creates a building model object.
     *
     * @param id          id of the building
     * @param name        name of the building
     * @param location    location of the building
     * @param description description of the building
     * @param open        opening time of the building
     * @param closed      closing time of the building
     */
    public Building(
        Long id,
        String name,
        String location,
        String description,
        String open,
        String closed,
        Set<Room> rooms
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.open = open;
        this.closed = closed;
        this.rooms.addAll(rooms);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getOpen() {
        return open;
    }

    public String getClosed() {
        return closed;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Returns the capacity of the largest room inside the building.
     *
     * @return the max capacity of a room in the building
     */
    public int getMaxCapacity() {
        if (rooms.size() == 0) {
            return 0;
        }
        Room max = Collections.max(rooms, new RoomCapacityComparator());
        return max.getCapacity();
    }
}
