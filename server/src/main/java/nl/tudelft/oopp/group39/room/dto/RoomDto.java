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

    /**
     * Creates a RoomDto object.
     */
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

    /**
     * Gets the building of the room.
     *
     * @return the building of the room
     */
    public Long getBuilding() {
        return building;
    }

    /**
     * Changes the building of the room.
     *
     * @param building the new building
     */
    public void setBuilding(Long building) {
        this.building = building;
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
     * @param name the new room name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param capacity the new capacity
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * Checks whether the room is only for staff, or also for students.
     *
     * @return true if the room is only for staff, false otherwise
     */
    public Boolean isOnlyStaff() {
        return onlyStaff;
    }

    /**
     * Changes the room so that it is accessible for everyone, or only for staff.
     *
     * @param onlyStaff true if you want the room to be only for staff, false otherwise
     */
    public void setOnlyStaff(Boolean onlyStaff) {
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
     * @param description the new description
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
        this.facilities = facilities;
    }

    /**
     * Gets the bookings for the room.
     *
     * @return a set with bookings for the room
     */
    public Set<BookingDto> getBookings() {
        return bookings;
    }

    /**
     * Changes the bookings for the room.
     *
     * @param bookings the new set of bookings
     */
    public void setBookings(Set<BookingDto> bookings) {
        this.bookings = bookings;
    }

    /**
     * Changes the RoomDto to a Room object.
     *
     * @return a Room object
     */
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
