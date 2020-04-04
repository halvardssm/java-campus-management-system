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
    public static final String COL_NAME = "name";
    public static final String COL_LOCATION = "location";
    public static final String COL_DESC = "description";
    public static final String COL_OPEN = "open";
    public static final String COL_CLOSED = "closed";
    public static final String COL_ROOMS = "rooms";
    public static final String COL_RESERVABLES = "reservables";

    private String name;
    private String location;
    private String description;
    private LocalTime open;
    private LocalTime closed;
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    private Set<Room> rooms = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER) //TODO change to reservable id
    private Set<Reservable> reservables = new HashSet<>();

    /**
     * Creates a building.
     */
    public Building() {
    }

    /**
     * Creates a building.
     *
     * @param id          the id of the building
     * @param name        the name of the building
     * @param location    the location of the building
     * @param description the description of the building
     * @param open        the opening time of the building
     * @param closed      the closing time of the building
     * @param rooms       the rooms of the building
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

    /**
     * Gets the name of the building.
     *
     * @return the name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the building.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the building.
     *
     * @return the location of the building
     */
    public String getLocation() {
        return location;
    }

    /**
     * Changes the location of the building.
     *
     * @param location the new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the description of the building.
     *
     * @return the description of the building
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of the building.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the opening time of the building.
     *
     * @return the opening time of the building
     */
    public LocalTime getOpen() {
        return open;
    }

    /**
     * Changes the opening time of the building.
     *
     * @param open the new opening time
     */
    public void setOpen(LocalTime open) {
        this.open = open;
    }

    /**
     * Gets the closing time of the building.
     *
     * @return the closing time of the building
     */
    public LocalTime getClosed() {
        return closed;
    }

    /**
     * Changes the closing time of the building.
     *
     * @param closed the new closing time
     */
    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

    /**
     * Gets the rooms of the building.
     *
     * @return a set with all the rooms from the building
     */
    public Set<Room> getRooms() {
        return rooms;
    }

    /**
     * Changes the rooms of the building.
     *
     * @param rooms The new rooms
     */
    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Gets the reservables for the building.
     *
     * @return a set with all the reservables
     */
    public Set<Reservable> getReservables() {
        return reservables;
    }

    /**
     * Changes the reservables of the building.
     *
     * @param reservables the new reservables
     */
    public void setReservables(Set<Reservable> reservables) {
        this.reservables = reservables;
    }

    /**
     * Checks whether two rooms are equal.
     *
     * @param o the other object to compare
     * @return true if the two buildings are the same, false otherwise
     */
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
