package nl.tudelft.oopp.group39.building.model;

import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.room.model.Room;

public class Building {

    private Integer id;

    private String name;

    private String location;

    private String description;

    private String open;

    private String closed;

    private Set<Room> rooms = new HashSet<>();

    public Building() {

    }

    /**
     * Doc. TODO Sven
     *
     * @param name        name
     * @param location    location
     * @param description description
     * @param open        open
     * @param closed      closed
     */
    public Building(
        Integer id,
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

    public Integer getId() {
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
}
