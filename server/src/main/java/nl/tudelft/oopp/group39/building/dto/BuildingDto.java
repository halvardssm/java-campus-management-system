package nl.tudelft.oopp.group39.building.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.room.dto.RoomDto;
import org.springframework.stereotype.Component;

@Component
public class BuildingDto {

    private Long id;
    private String name;
    private String location;
    private String description;
    private LocalTime open;
    private LocalTime closed;
    private Set<RoomDto> rooms = new HashSet<>();
    private Set<Long> reservables = new HashSet<>();

    public BuildingDto() {
    }

    /**
     * Constructor for BuildingDto.
     *
     * @param name name of building
     * @param location location value of building
     * @param description description of building
     * @param open opening time of the building
     * @param closed closing time of the building
     * @param rooms the set of rooms contained in building (in RoomDto form)
     * @param reservables TODO
     *
     * @see RoomDto
     */
    public BuildingDto(
        Long id,
        String name,
        String location,
        String description,
        LocalTime open,
        LocalTime closed,
        Set<RoomDto> rooms,
        Set<Long> reservables
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.open = open;
        this.closed = closed;
        this.rooms.addAll(initSet(rooms));
        this.reservables.addAll(initSet(reservables));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public LocalTime getClosed() {
        return closed;
    }

    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

    public Set<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(Set<RoomDto> roomDtos) {
        this.rooms = roomDtos;
    }

    public Set<Long> getReservables() {
        return reservables;
    }

    public void setReservables(Set<Long> reservables) {
        this.reservables = reservables;
    }

}
