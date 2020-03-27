package nl.tudelft.oopp.group39.building.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.room.entities.Room;

@Entity
@Table(name = Building.TABLE_NAME)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = Building.COL_ID
)
public class Building {
    public static final String TABLE_NAME = "buildings";
    public static final String MAPPED_NAME = "building";
    public static final String COL_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;
    private String description;
    private LocalTime open;
    private LocalTime closed;
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.LAZY)
    private Set<Room> rooms = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME) //TODO change to reservable id
    private Set<Reservable> reservables = new HashSet<>();

    public Building() {
    }

    /**
     * Constructor. TODO Sven
     *
     * @param name        name
     * @param location    location
     * @param description description
     * @param open        open
     * @param closed      closed
     * @param rooms       rooms
     */
    public Building(
        String name,
        String location,
        String description,
        LocalTime open,
        LocalTime closed,
        Set<Room> rooms,
        Set<Reservable> reservables
    ) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.open = open;
        this.closed = closed;
        this.rooms.addAll(initSet(rooms));
        this.reservables.addAll(initSet(reservables));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public LocalTime getClosed() {
        return closed;
    }

    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public Set<Reservable> getReservables() {
        return reservables;
    }

    public void setReservables(Set<Reservable> reservables) {
        this.reservables = reservables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        Building building = (Building) o;
        return getId() == building.getId()
            && Objects.equals(getName(), building.getName())
            && Objects.equals(getLocation(), building.getLocation())
            && Objects.equals(getDescription(), building.getDescription())
            && Objects.equals(getOpen(), building.getOpen())
            && Objects.equals(getClosed(), building.getClosed())
            && Objects.equals(getReservables(), building.getReservables());
    }
}
