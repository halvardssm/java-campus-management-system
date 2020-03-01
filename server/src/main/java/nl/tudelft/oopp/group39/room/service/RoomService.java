package nl.tudelft.oopp.group39.room.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.group39.building.repositories.BuildingRepository;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.service.FacilityService;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.exceptions.RoomExistsException;
import nl.tudelft.oopp.group39.room.exceptions.RoomNotFoundException;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
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
            throw new RoomExistsException((int) newRoom.getId());
        } catch (RoomNotFoundException e) {
            roomRepository.save(newRoom);
            return newRoom;

        }
    }

    public Room updateRoom(Room newRoom, int id) throws RoomNotFoundException {
        return roomRepository.findById((long) id)
            .map(room -> roomRepository.save(newRoom)).orElseThrow(() -> new RoomNotFoundException(id));
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

    //Function for updating a room
//    public void updateRoom(long id, int capacity, boolean onlyStaff, Set<Facility> facilities, long buildingId, String description) {
//        int[] facilityIds = roomFacilityRepository.getRoomFacilityIdsByRoomId(id);
//        Room n = new Room(id,buildingId,capacity,onlyStaff,description, facilities);
//        n = updateRoom(n, (int) id);
//    }

}
