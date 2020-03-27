package nl.tudelft.oopp.group39.room.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.AbstractEntity;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.room.dto.RoomDto;

@Entity
@Table(name = Room.TABLE_NAME)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.None.class,
    property = Room.COL_ID
)
public class Room extends AbstractEntity {
    public static final String TABLE_NAME = "rooms";
    public static final String MAPPED_NAME = "room";
    public static final String COL_CAPACITY = "capacity";
    public static final String COL_ONLY_STAFF = "onlyStaff";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";

    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = Building.MAPPED_NAME)
    private Building building;
    private int capacity;
    private Boolean onlyStaff;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
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

    @ManyToMany(mappedBy = TABLE_NAME, fetch = FetchType.LAZY)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.LAZY)
    private Set<Booking> bookings = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME)
    private Set<Reservation> reservations = new HashSet<>();

    public Room() {
    }

    /**
     * Creates a room.
     *
     * @param building    the building
     * @param name        name of the room
     * @param capacity    capacity of the room
     * @param onlyStaff   whether the room is only accessible to staff
     * @param description description of the room
     * @param facilities  set of facilities the room has
     * @param bookings    set of bookings for the room
     */
    public Room(
        Building building,
        String name,
        int capacity,
        boolean onlyStaff,
        String description,
        Set<Facility> facilities,
        Set<Booking> bookings
    ) {
        this.building = building;
        this.name = name;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities.addAll(initSet(facilities));
        this.bookings.addAll(initSet(bookings));
    }

    /**
     * Converts the Room entity to the RoomDto model for JSON serializing.
     *
     * @return converted RoomDto
     */
    public RoomDto toDto() {
        Set<BookingDto> bookingDtos = new HashSet<>();
        bookings.forEach(booking -> bookingDtos.add(booking.toDto()));

        return new RoomDto(
            id,
            building.getId(),
            name,
            capacity,
            onlyStaff,
            description,
            facilities,
            bookingDtos);
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return getId().equals(room.getId())
            && building.equals(room.building)
            && getCapacity() == room.getCapacity()
            && getOnlyStaff() == room.getOnlyStaff()
            && Objects.equals(getName(), room.getName())
            && Objects.equals(getDescription(), room.getDescription())
            && Objects.equals(getFacilities(), room.getFacilities())
            && Objects.equals(getBookings(), room.getBookings())
            && Objects.equals(getReservations(), room.getReservations());
    }
}
