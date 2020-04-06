package nl.tudelft.oopp.group39.room.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.services.FacilityService;
import nl.tudelft.oopp.group39.room.dao.RoomDao;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private FacilityService facilityService;

    /**
     * Reads a room.
     *
     * @param id the room to be read
     * @return the requested room
     * @throws NotFoundException if the room wasn't found
     */
    public Room readRoom(Long id) throws NotFoundException {
        return roomRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Room.MAPPED_NAME, id));
    }

    /**
     * Lists all rooms.
     *
     * @return a list of rooms
     */
    public List<Room> listRooms(Map<String, String> params) {
        return roomDao.roomFilter(params);
    }

    /**
     * Create a room.
     *
     * @return the created room
     */
    public Room createRoom(Room newRoom) {
        return roomRepository.save(newRoom);
    }

    /**
     * Update a room.
     *
     * @return the updated room
     * @throws NotFoundException if the room wasn't found
     */
    public Room updateRoom(Room newRoom, Long id) throws NotFoundException {
        return roomRepository.findById(id)
            .map(room -> {
                mapFacilitiesForRooms(newRoom);
                room.setName(newRoom.getName());
                room.setDescription(newRoom.getDescription());
                room.setCapacity(newRoom.getCapacity());
                room.setOnlyStaff(newRoom.getOnlyStaff());
                room.setBuilding(newRoom.getBuilding());
                room.setFacilities(newRoom.getFacilities());

                if (newRoom.getImage() != null) {
                    room.setImage(newRoom.getImage());
                }

                return roomRepository.save(room);
            }).orElseThrow(() -> new NotFoundException(Room.MAPPED_NAME, id));
    }

    /**
     * Delete a room.
     *
     * @throws NotFoundException if the room wasn't found
     */
    public Room deleteRoom(Long id) throws NotFoundException {
        try {
            Room rf = readRoom(id);
            roomRepository.delete(readRoom(id));
            return rf;
        } catch (NotFoundException e) {
            throw new NotFoundException(Room.MAPPED_NAME, id);
        }
    }

    /**
     * Will map the roles of a user to the roles in the db.
     *
     * @param room A user to map roles for
     */
    protected void mapFacilitiesForRooms(Room room) {
        Set<Facility> facilities = new HashSet<>();
        for (Facility facility : room.getFacilities()) {
            Facility mappedFacility = facilityService.readFacility(facility.getId());

            if (mappedFacility != null) {
                facilities.add(mappedFacility);
            }
        }
        room.getFacilities().clear();
        room.getFacilities().addAll(facilities);
    }
}
