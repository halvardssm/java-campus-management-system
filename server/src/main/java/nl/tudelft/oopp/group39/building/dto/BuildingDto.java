package nl.tudelft.oopp.group39.building.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.room.dto.RoomDto;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class BuildingDto extends AbstractDto<Building, BuildingDto> {

    private String name;
    private String location;
    private String description;
    private LocalTime open;
    private LocalTime closed;
    private Set<RoomDto> rooms = new HashSet<>();

    public BuildingDto() {
    }

    /**
     * Constructor for BuildingDto.
     *
     * @param name        name of building
     * @param location    location value of building
     * @param description description of building
     * @param open        opening time of the building
     * @param closed      closing time of the building
     * @param rooms       the set of rooms contained in building (in RoomDto form)
     * @see RoomDto
     */
    public BuildingDto(
        Long id,
        String name,
        String location,
        String description,
        LocalTime open,
        LocalTime closed,
        Set<RoomDto> rooms
    ) {
        setId(id);
        setName(name);
        setLocation(location);
        setDescription(description);
        setOpen(open);
        setClosed(closed);
        getRooms().addAll(initSet(rooms));
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

    @Override
    public Building toEntity() {
        Set<Room> rooms1 = new HashSet<>();

        if (getRooms() != null) {
            rooms1.addAll(Utils.setDtoToEntity(getRooms()));
        }

        return new Building(
            getId(),
            getName(),
            getLocation(),
            getDescription(),
            getOpen(),
            getClosed(),
            rooms1,
            null
        );
    }
}
