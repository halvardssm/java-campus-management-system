package nl.tudelft.oopp.group39.room.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import org.springframework.stereotype.Component;

@Component
public class RoomDto {

    private Long id;
    private Long building;
    private String name;
    private int capacity;
    private boolean onlyStaff;
    private String description;
    private Set<Facility> facilities = new HashSet<>();
    private Set<BookingDto> bookings = new HashSet<>();

    public RoomDto() {
    }

    /**
     * Creates a RoomDto object.
     *
     * @param id the id of the Room
     * @param building building id that contains the room
     * @param name the name of the room
     * @param capacity the capacity of the room
     * @param onlyStaff value that determines if the room is only for staff or not
     * @param description a description for the room
     * @param facilities the facilities that are contained in the room
     * @param bookings the bookings made for the room (in dto form)
     */
    public RoomDto(
        Long id,
        Long building,
        String name,
        int capacity,
        boolean onlyStaff,
        String description,
        Set<Facility> facilities,
        Set<BookingDto> bookings
    ) {
        this.id = id;
        this.building = building;
        this.name = name;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities.addAll(facilities);
        this.bookings.addAll(initSet(bookings));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuilding() {
        return building;
    }

    public void setBuilding(Long building) {
        this.building = building;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isOnlyStaff() {
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
        this.facilities = facilities;
    }

    public Set<BookingDto> getBookings() {
        return bookings;
    }

    public void setBookings(Set<BookingDto> bookings) {
        this.bookings = bookings;
    }
}
