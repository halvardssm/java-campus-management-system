package nl.tudelft.oopp.room.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tudelft.oopp.building.repositories.BuildingRepository;
import nl.tudelft.oopp.facility.entities.Facility;
import nl.tudelft.oopp.facility.services.FacilityService;
import nl.tudelft.oopp.room.entities.Room;
import nl.tudelft.oopp.room.exceptions.RoomExistsException;
import nl.tudelft.oopp.room.exceptions.RoomNotFoundException;
import nl.tudelft.oopp.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private FacilityService facilityService;

    public Room readRoom(long id) throws RoomNotFoundException {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException((int) id));
    }

    public List<Room> listRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room newRoom) {
        try {
            Room room = readRoom((int) newRoom.getId());
            throw new RoomExistsException((int) room.getId());
        } catch (RoomNotFoundException e) {
            mapFacilitiesForRooms(newRoom);
            roomRepository.save(newRoom);

            return newRoom;
        }
    }

    public Room updateRoom(Room newRoom, int id) throws RoomNotFoundException {
        return roomRepository.findById((long) id)
            .map(room -> {
                newRoom.setId(id);
                room = newRoom;
                mapFacilitiesForRooms(room);

                return roomRepository.save(room);
            }).orElseThrow(() -> new RoomNotFoundException(id));
    }

    // Method to filter rooms based on capacity, a room being accessible to students or not, the facilities that
    // should be present (if so their facility ids should be in the facilities array), the building name and a
    // string of the inputted location
    public List<Room> filterRooms(int capacity, boolean onlyStaff, int[] facilities, String building, String location, LocalTime open, LocalTime closed) {
        List<Facility> nFacilities = new ArrayList<>();
        for (long facility : facilities) {
            nFacilities.add(facilityService.readFacility(facility));
        }
        List<Long> resRoomIds = buildingRepository.filterBuildingsOnLocationAndNameAndTimeList(location, building, open, closed);
        return (resRoomIds.size() > 0 ? roomRepository.filterRooms(capacity, onlyStaff, resRoomIds, nFacilities) : new ArrayList<Room>());
    }

    public Room deleteRoom(int id) throws RoomNotFoundException {
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
