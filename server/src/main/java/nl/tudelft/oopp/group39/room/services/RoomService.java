package nl.tudelft.oopp.group39.room.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.services.FacilityService;
import nl.tudelft.oopp.group39.room.dao.RoomDao;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.exceptions.RoomNotFoundException;
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

    public Room readRoom(Long id) throws RoomNotFoundException {
        return roomRepository.findById(id)
            .orElseThrow(() -> new RoomNotFoundException(id));
    }

    public List<Room> listRooms() {
        return roomRepository.findAll();
    }

    /**
     * Doc. TODO Sven
     */
    public Room createRoom(Room newRoom) {
        return roomRepository.save(newRoom);
    }

    /**
     * Doc. TODO Sven
     */
    public Room updateRoom(Room newRoom, Long id) throws RoomNotFoundException {
        return roomRepository.findById(id)
            .map(room -> {
                newRoom.setId(id);
                room = newRoom;
                mapFacilitiesForRooms(room);

                return roomRepository.save(room);
            }).orElseThrow(() -> new RoomNotFoundException(id));
    }

    /**
     * Doc. TODO Sven
     * Method to filter rooms.
     * Based on capacity, a room being accessible to students or not, the facilities that should
     * be present (if so their facility ids should be in the facilities array), the building name
     * and a string of the inputted location
     */
    public List<Room> filterRooms(Map<String,String> allParams) {
        return roomDao.roomFilter(allParams);
    }

    /**
     * Doc. TODO Sven
     */
    public Room deleteRoom(Long id) throws RoomNotFoundException {
        try {
            Room rf = readRoom(id);
            roomRepository.delete(readRoom(id));
            return rf;
        } catch (RoomNotFoundException e) {
            throw new RoomNotFoundException(id);
        }
    }

    /**
     * Will map the roles of a user to the roles in the db.
     *
     * @param room A user to map roles for
     */
    private void mapFacilitiesForRooms(Room room) {
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
