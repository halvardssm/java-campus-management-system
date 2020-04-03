package nl.tudelft.oopp.group39.building.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.dto.BuildingDto;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.room.dto.RoomDto;
import nl.tudelft.oopp.group39.room.entities.Room;

@Entity
@Table(name = Building.TABLE_NAME)
public class Building extends AbstractEntity<Building, BuildingDto> {
    public static final String TABLE_NAME = "buildings";
    public static final String MAPPED_NAME = "building";
    public static final String COL_OPEN = "open";
    public static final String COL_CLOSED = "closed";
    public static final String COL_NAME = "name";
    public static final String COL_LOCATION = "location";
    public static final String COL_DESC = "description";

    private String name;
    private String location;
    private String description;
    private LocalTime open;
    private LocalTime closed;
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    private Set<Room> rooms = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME) //TODO change to reservable id
    private Set<Reservable> reservables = new HashSet<>();

    public Building() {
    }

    /**
     * Constructor. TODO Sven
     *
     * @param id          id
     * @param name        name
     * @param location    location
     * @param description description
     * @param open        open
     * @param closed      closed
     * @param rooms       rooms
     */
    public Building(
        Long id,
        String name,
        String location,
        String description,
        LocalTime open,
        LocalTime closed,
        Set<Room> rooms,
        Set<Reservable> reservables
    ) {
        setId(id);
        setName(name);
        setLocation(location);
        setDescription(description);
        setOpen(open);
        setClosed(closed);
        getRooms().addAll(initSet(rooms));
        getReservables().addAll(initSet(reservables));
    }

    /**
     * Method to convert a Building object to BuildingDto for JSON serializing.
     *
     * @return the BuildingDto converted from building
     */
    public BuildingDto toDto() {
        Set<RoomDto> roomDtoSet = Utils.setEntityToDto(rooms);

        return new BuildingDto(
            id,
            name,
            location,
            description,
            open,
            closed,
            roomDtoSet
        );
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Building building = (Building) o;
        return Objects.equals(getName(), building.getName())
            && Objects.equals(getLocation(), building.getLocation())
            && Objects.equals(getDescription(), building.getDescription())
            && Objects.equals(getOpen(), building.getOpen())
            && Objects.equals(getClosed(), building.getClosed())
            && Objects.equals(getRooms(), building.getRooms())
            && Objects.equals(getReservables(), building.getReservables());
    }
}
