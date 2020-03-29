package nl.tudelft.oopp.group39.room.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomDto extends AbstractDto<Room, RoomDto> {

    private Long building;
    private String name;
    private Integer capacity;
    private Boolean onlyStaff;
    private String description;
    private Set<Facility> facilities = new HashSet<>();
    private Set<BookingDto> bookings = new HashSet<>();

    public RoomDto() {
    }

    /**
     * Creates a RoomDto object.
     *
     * @param id          the id of the Room
     * @param building    building id that contains the room
     * @param name        the name of the room
     * @param capacity    the capacity of the room
     * @param onlyStaff   value that determines if the room is only for staff or not
     * @param description a description for the room
     * @param facilities  the facilities that are contained in the room
     * @param bookings    the bookings made for the room (in dto form)
     */
    public RoomDto(
        Long id,
        Long building,
        String name,
        Integer capacity,
        Boolean onlyStaff,
        String description,
        Set<Facility> facilities,
        Set<BookingDto> bookings
    ) {
        setId(id);
        setBuilding(building);
        setName(name);
        setCapacity(capacity);
        setOnlyStaff(onlyStaff);
        setDescription(description);
        getFacilities().addAll(facilities);
        getBookings().addAll(initSet(bookings));

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean isOnlyStaff() {
        return onlyStaff;
    }

    public void setOnlyStaff(Boolean onlyStaff) {
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

    @Override
    public Room toEntity() {
        return new Room(
            getId(),
            Utils.idToEntity(getBuilding(), Building.class),
            getName(),
            getCapacity(),
            isOnlyStaff(),
            getDescription(),
            null,
            getFacilities(),
            Utils.setDtoToEntity(getBookings())
        );
    }
}
