package nl.tudelft.oopp.group39.facility.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.room.entities.Room;

@Entity
@Table(name = Facility.TABLE_NAME)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.None.class,
    property = Facility.COL_ID
)
@JsonIgnoreProperties(allowSetters = true, value = {Facility.COL_ROOMS})
public class Facility {
    public static final String TABLE_NAME = "facilities";
    public static final String MAPPED_NAME = "facility";
    public static final String COL_ID = "id";
    public static final String COL_ROOMS = "rooms";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String description;

    @ManyToMany(mappedBy = Facility.TABLE_NAME, fetch = FetchType.LAZY)
    private Set<Room> rooms = new HashSet<>();

    public Facility() {
    }

    public Facility(String description, Set<Room> rooms) {
        this.description = description;
        this.rooms.addAll(initSet(rooms));
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
