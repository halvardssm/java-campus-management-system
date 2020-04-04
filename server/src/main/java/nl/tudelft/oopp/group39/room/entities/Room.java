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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = Building.MAPPED_NAME)
    private Building building;
    private String name;
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

    /**
     * Creates a room.
     */
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

    /**
     * Gets the name of the room.
     *
     * @return the room name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the room.
     *
     * @param name the new name of the room
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the building of the room.
     *
     * @return the building of the room
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Changes the building of the room.
     *
     * @param building the new building of the room
     */
    public void setBuilding(Building building) {
        this.building = building;
    }

    /**
     * Gets the capacity of the room.
     *
     * @return the capacity of the room
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Changes the capacity of the room.
     *
     * @param capacity the new capacity of the room
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * Checks whether the room is only for staff, or also for students.
     *
     * @return true if the room is only for staff, false otherwise
     */
    public boolean getOnlyStaff() {
        return onlyStaff;
    }

    /**
     * Changes whether the room is only accessible for staff, or also for students.
     *
     * @param onlyStaff true if you want the room to be only accessible for staff, false otherwise
     */
    public void setOnlyStaff(boolean onlyStaff) {
        this.onlyStaff = onlyStaff;
    }

    /**
     * Gets the description of the room.
     *
     * @return the description of the room
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of the room.
     *
     * @param description the new description of the room
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the facilities that the room has to offer.
     *
     * @return a set with the facilities that the room has to offer
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * Changes the facilities that the room has to offer.
     *
     * @param facilities the new set of facilities
     */
    public void setFacilities(Set<Facility> facilities) {
        this.facilities = facilities == null ? new HashSet<>() : facilities;
    }

    /**
     * Gets the events for the room.
     *
     * @return a set with the events for the room
     */
    public Set<Event> getEvents() {
        return events;
    }

    /**
     * Changes the events for the room.
     *
     * @param events the new set of events for the room
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     * Gets the bookings for the room.
     *
     * @return a set with the bookings for the room
     */
    public Set<Booking> getBookings() {
        return bookings;
    }

    /**
     * Changes the bookings for the room.
     *
     * @param bookings the new set of bookings for the room
     */
    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * Gets the reservations for the room.
     *
     * @return a set with the reservations for the room
     */
    public Set<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Changes the reservations for the room.
     *
     * @param reservations the new set of reservations
     */
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

    /**
     * Checks whether two rooms are equal.
     *
     * @param o the other object
     * @return  true if the two rooms are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(getId(), room.getId())
            && Objects.equals(getName(), room.getName())
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
