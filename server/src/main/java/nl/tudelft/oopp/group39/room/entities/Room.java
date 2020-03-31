package nl.tudelft.oopp.group39.room.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.room.dto.RoomDto;

@Entity
@Table(name = Room.TABLE_NAME)
@JsonIdentityInfo(generator = ObjectIdGenerators.None.class)
public class Room extends AbstractEntity<Room, RoomDto> {
    public static final String TABLE_NAME = "rooms";
    public static final String MAPPED_NAME = "room";
    public static final String COL_CAPACITY = "capacity";
    public static final String COL_ONLY_STAFF = "onlyStaff";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";

    private String name;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = Building.MAPPED_NAME)
    private Building building;
    private Integer capacity;
    private Boolean onlyStaff;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = (TABLE_NAME + "_" + Facility.TABLE_NAME),
        joinColumns = {
            @JoinColumn(name = TABLE_NAME, referencedColumnName = COL_ID,
                nullable = false, updatable = false)
        },
        inverseJoinColumns = {
            @JoinColumn(name = Facility.TABLE_NAME, referencedColumnName = Facility.COL_ID,
                nullable = false, updatable = false)
        })
    private Set<Facility> facilities = new HashSet<>();

    @ManyToMany(mappedBy = TABLE_NAME, fetch = FetchType.EAGER)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    private Set<Booking> bookings = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    private Set<Reservation> reservations = new HashSet<>();

    public Room() {
    }

    /**
     * Creates a room.
     *
     * @param id          the id
     * @param building    the building
     * @param name        name of the room
     * @param capacity    capacity of the room
     * @param onlyStaff   whether the room is only accessible to staff
     * @param description description of the room
     * @param events      set of events the room has
     * @param facilities  set of facilities the room has
     * @param bookings    set of bookings for the room
     */
    public Room(
        Long id,
        Building building,
        String name,
        Integer capacity,
        Boolean onlyStaff,
        String description,
        Set<Event> events,
        Set<Facility> facilities,
        Set<Booking> bookings
    ) {
        setId(id);
        setBuilding(building);
        setName(name);
        setCapacity(capacity);
        setOnlyStaff(onlyStaff);
        setDescription(description);
        getEvents().addAll(initSet(events));
        getFacilities().addAll(initSet(facilities));
        getBookings().addAll(initSet(bookings));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public boolean getOnlyStaff() {
        return onlyStaff;
    }

    public void setOnlyStaff(boolean onlyStaff) {
        this.onlyStaff = onlyStaff;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(Set<Facility> facilities) {
        this.facilities = facilities == null ? new HashSet<>() : facilities;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * Converts the Room entity to the RoomDto model for JSON serializing.
     *
     * @return converted RoomDto
     */
    @Override
    public RoomDto toDto() {
        return new RoomDto(
            getId(),
            getBuilding().getId(),
            getName(),
            getCapacity(),
            getOnlyStaff(),
            getDescription(),
            getFacilities(),
            Utils.setEntityToDto(getBookings())
        );
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
        return Objects.equals(getName(), room.getName())
            && Objects.equals(getBuilding(), room.getBuilding())
            && Objects.equals(getCapacity(), room.getCapacity())
            && Objects.equals(getOnlyStaff(), room.getOnlyStaff())
            && Objects.equals(getDescription(), room.getDescription())
            && Objects.equals(getFacilities(), room.getFacilities())
            && Objects.equals(getEvents(), room.getEvents())
            && Objects.equals(getBookings(), room.getBookings())
            && Objects.equals(getReservations(), room.getReservations());
    }
}
