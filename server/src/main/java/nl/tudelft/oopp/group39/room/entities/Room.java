package nl.tudelft.oopp.group39.room.entities;

import nl.tudelft.oopp.group39.facility.entities.Facility;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Room.TABLE_NAME)
public class Room {
    public static final String TABLE_NAME = "rooms";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long buildingId;

    private int capacity;

    private boolean onlyStaff;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "rooms_facilities", joinColumns = {@JoinColumn(name = "room_id")}, inverseJoinColumns = {@JoinColumn(name = "facility_id")})
    private Set<Facility> facilities = new HashSet<>();

    public Room() {
    }

    public Room(long buildingId, int capacity, boolean onlyStaff, String description, Set<Facility> facilities) {
        this.buildingId = buildingId;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities = facilities == null ? new HashSet<>() : facilities;
    }

    public long getId() {
        return id;
    }

    public long getBuilding() {
        return buildingId;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getOnlyStaff() {
        return onlyStaff;
    }

    public String getDescription() {
        return description;
    }

    public Set<Facility> getFacilities() {
        return facilities;
    }

    public void setBuilding(long buildingId) {
        this.buildingId = buildingId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setOnlyStaff(boolean onlyStaff) {
        this.onlyStaff = onlyStaff;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFacilities(Set<Facility> facilities) {
        this.facilities = facilities == null ? new HashSet<>() : facilities;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Room room = (Room) o;

        //return id == room.id;

        boolean equals = (capacity == room.capacity) && (onlyStaff == room.onlyStaff);
        equals = equals && (room.description.contentEquals(description));
        equals = equals && (buildingId == room.buildingId) && (id == room.id);
        return equals;
    }
}
