package nl.tudelft.oopp.group39.facility.entities;

import nl.tudelft.oopp.group39.room.entities.Room;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = Facility.TABLE_NAME)
public class Facility {
    public static final String TABLE_NAME = "facilities";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToMany(mappedBy = "facilities", fetch = FetchType.LAZY)
    private Set<Room> rooms = new HashSet<>();

    public Facility() {
    }

    public Facility(String description, Set<Room> rooms) {
        this.description = description;
        this.rooms.addAll(rooms);
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms.size() == 0 ? new HashSet<>() : rooms;
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
        return getId() == facility.getId()
            && Objects.equals(getDescription(), facility.getDescription())
            && rooms.equals(facility.rooms);
    }
}
