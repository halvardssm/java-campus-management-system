package nl.tudelft.oopp.group39.room.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.facility.entities.Facility;

@Entity
@Table(name = Room.TABLE_NAME)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = Room.COL_ID
)
public class Room {
    public static final String TABLE_NAME = "rooms";
    public static final String MAPPED_NAME = "room";
    public static final String COL_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long buildingId;

    private int capacity;
    private String name;
    private boolean onlyStaff;

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
    @JsonIgnore
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Booking> bookings = new HashSet<>();

    public Room() {
    }

    public Room(long buildingId, int capacity, String name, boolean onlyStaff, String description, Set<Facility> facilities, Set<Booking> bookings) {
        this.buildingId = buildingId;
        this.capacity = capacity;
        this.name = name;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities.addAll(facilities != null ? facilities : new HashSet<>());
        this.bookings.addAll(bookings != null ? bookings : new HashSet<>());
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

    public long getBuilding() {
        return buildingId;
    }

    public void setBuilding(long buildingId) {
        this.buildingId = buildingId;
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
            && buildingId == room.buildingId
            && getCapacity() == room.getCapacity()
            && getOnlyStaff() == room.getOnlyStaff()
            && Objects.equals(getDescription(), room.getDescription())
            && Objects.equals(getFacilities(), room.getFacilities())
            && Objects.equals(getBookings(), room.getBookings());
    }
}
