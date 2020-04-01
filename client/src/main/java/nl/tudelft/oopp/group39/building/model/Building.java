package nl.tudelft.oopp.group39.building.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import nl.tudelft.oopp.group39.room.comparator.RoomCapacityComparator;
import nl.tudelft.oopp.group39.room.model.Room;

public class Building {
    private Long id;
    private String name;
    private String location;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime open;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime closed;

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
        LocalTime open,
        LocalTime closed,
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

    public LocalTime getOpen() {
        return open;
    }

    public LocalTime getClosed() {
        return closed;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Building building = (Building) o;

        return id.equals(building.id)
            && name.equals(building.name)
            && location.equals(building.location)
            && description.equals(building.description)
            && open.equals(building.open)
            && closed.equals(building.closed)
            && Arrays.deepEquals(building.rooms.toArray(),rooms.toArray());
    }

    /**
     * Returns the capacity of the largest room inside the building.
     *
     * @return the max capacity of a room in the building
     */
    @JsonIgnore
    public int getMaxCapacity() {
        if (rooms.size() == 0) {
            return 0;
        }
        Room max = Collections.max(rooms, new RoomCapacityComparator());
        return max.getCapacity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
