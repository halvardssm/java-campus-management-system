package nl.tudelft.oopp.group39.room.entities;

import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.facility.entities.Facility;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = Room.TABLE_NAME)
public class Room {
    public static final String TABLE_NAME = "rooms";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long buildingId;

    private int capacity;

    private boolean onlyStaff;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "rooms_facilities",
        joinColumns = {
            @JoinColumn(name = "room_id", referencedColumnName = "id",
                nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "facility_id", referencedColumnName = "id",
                nullable = false, updatable = false)})
    private Set<Facility> facilities = new HashSet<>();

    @OneToMany(mappedBy = "room")
    private Set<Booking> bookings = new HashSet<>();

    public Room() {
    }

    public Room(long buildingId, int capacity, boolean onlyStaff, String description, Set<Facility> facilities, Set<Booking> bookings) {
        this.buildingId = buildingId;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities.addAll(facilities);
        this.bookings.addAll(bookings);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
