package nl.tudelft.oopp.group39.room.dto;

import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import org.springframework.stereotype.Component;

@Component
public class RoomDto {

    private Integer id;
    private Integer building;
    private String name;
    private int capacity;
    private boolean onlyStaff;
    private String description;
    private Set<Facility> facilities = new HashSet<>();
    private Set<BookingDto> bookings = new HashSet<>();

    public RoomDto() {
    }

    public RoomDto(
        Integer id,
        Integer building,
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
        this.bookings.addAll(bookings);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBuilding() {
        return building;
    }

    public void setBuilding(Integer building) {
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
