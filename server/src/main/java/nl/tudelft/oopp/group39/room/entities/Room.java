package nl.tudelft.oopp.group39.room.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;

@Entity
@Table(name = Room.TABLE_NAME)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = Room.COL_ID
)
public class Room {
    public static final String TABLE_NAME = "rooms";
    public static final String MAPPED_NAME = "room";
    public static final String COL_ID = "id";
    public static final String CAPACITY = "capacity";
    public static final String ONLY_STAFF = "only_staff";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_ID)
    private long id;
    @Column(name = NAME)
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = Building.MAPPED_NAME)
    @JsonIgnore
    private Building building;
    @Column(name = CAPACITY)
    private int capacity;
    @Column(name = ONLY_STAFF)
    private boolean onlyStaff;
    @Column(name = DESCRIPTION)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = TABLE_NAME + "_" + Facility.TABLE_NAME,
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
        return getId() == room.getId()
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
