package nl.tudelft.oopp.group39.building.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.sql.Blob;
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
    private Blob image;
    private Set<RoomDto> rooms = new HashSet<>();

    /**
     * Constructor for BuildingDto.
     */
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
     * @param image       the image
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
        Blob image,
        Set<RoomDto> rooms
    ) {
        setId(id);
        setName(name);
        setLocation(location);
        setDescription(description);
        setOpen(open);
        setClosed(closed);
        setImage(image);
        getRooms().addAll(initSet(rooms));
    }

    /**
     * Gets the name of the building.
     *
     * @return the name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the building.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the building.
     *
     * @return the location of the building
     */
    public String getLocation() {
        return location;
    }

    /**
     * Changes the address of the building.
     *
     * @param location the new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the description of the building.
     *
     * @return the description of the building
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of the building.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the opening time of the building.
     *
     * @return the opening time of the building
     */
    public LocalTime getOpen() {
        return open;
    }

    /**
     * Changes the opening time of the building.
     *
     * @param open the new opening time
     */
    public void setOpen(LocalTime open) {
        this.open = open;
    }

    /**
     * Gets the closing time of the building.
     *
     * @return the closing time of the building
     */
    public LocalTime getClosed() {
        return closed;
    }

    /**
     * Changes the closing time of the building.
     *
     * @param closed the new closing time
     */
    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    /**
     * Gets the rooms of the building.
     *
     * @return a set with the rooms of the building
     */
    public Set<RoomDto> getRooms() {
        return rooms;
    }

    /**
     * Changes the rooms of the building.
     *
     * @param roomDtos the new set of rooms
     */
    public void setRooms(Set<RoomDto> roomDtos) {
        this.rooms = roomDtos;
    }

    /**
     * Changes the BuildingDto to Building object.
     *
     * @return the Building object
     */
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
            getImage(),
            rooms1,
            null
        );
    }
}
