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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.config.abstracts.IEntity;
import nl.tudelft.oopp.group39.room.entities.Room;

@Entity
@Table(name = Facility.TABLE_NAME)
@JsonIdentityInfo(generator = ObjectIdGenerators.None.class)
@JsonIgnoreProperties(allowSetters = true, value = {Facility.COL_ROOMS})
public class Facility extends AbstractEntity<Facility, IEntity> {
    public static final String TABLE_NAME = "facilities";
    public static final String MAPPED_NAME = "facility";
    public static final String COL_ID = "id";
    public static final String COL_ROOMS = "rooms";

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String description;

    @ManyToMany(mappedBy = Facility.TABLE_NAME, fetch = FetchType.EAGER)
    private Set<Room> rooms = new HashSet<>();

    /**
     * Creates a facility.
     */
    public Facility() {
    }

    /**
     * Creates a facility.
     *
     * @param description a description of the facilities
     * @param rooms       the rooms where the facility is available
     */
    public Facility(String description, Set<Room> rooms) {
        this.description = description;
        this.rooms.addAll(initSet(rooms));
    }

    /**
     * Gets the description of the facility.
     *
     * @return the description of the facility
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of the facility.
     *
     * @param description the new description of the facility
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Changes the rooms where a facility is available.
     *
     * @param rooms the new set of rooms
     */
    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms.size() == 0 ? new HashSet<>() : rooms;
    }

    /**
     * Changes to a Dto object.
     *
     * @return null
     */
    @Override
    public IEntity toDto() {
        return null;
    }

    /**
     * Checks whether two facilities are equal.
     *
     * @param o the other object
     * @return  true if the two facilities are equal, false otherwise
     */
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
