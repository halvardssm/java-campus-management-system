package nl.tudelft.oopp.group39.facility.Entities;

import nl.tudelft.oopp.group39.room.Entities.Room;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "facilities")
    private Set<Room> rooms = new HashSet<>();

    public Facility() {
    }

    public Facility(long id, String description, Set<Room> rooms) {
        this.id = id;
        this.description = description;
        this.rooms = rooms == null ? new HashSet<>() : rooms;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facility facility = (Facility) o;
        return getId() == facility.getId() &&
            Objects.equals(getDescription(), facility.getDescription()) &&
            rooms.equals(facility.rooms);
    }
}
