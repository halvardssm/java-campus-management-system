package nl.tudelft.oopp.group39.room.Service;

import nl.tudelft.oopp.group39.building.Repositories.BuildingRepository;
import nl.tudelft.oopp.group39.room.Entities.Room;
import nl.tudelft.oopp.group39.room.Exceptions.RoomExistsException;
import nl.tudelft.oopp.group39.room.Exceptions.RoomNotFoundException;
import nl.tudelft.oopp.group39.room.Repositories.RoomRepository;
import nl.tudelft.oopp.group39.roomFacility.Exceptions.RoomFacilityNotFoundException;
import nl.tudelft.oopp.group39.roomFacility.Repositories.RoomFacilityRepository;
import nl.tudelft.oopp.group39.roomFacility.RoomFacilityService.RoomFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private RoomFacilityService rfService;

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
                .map(room -> {
                    room.setBuilding(newRoom.getBuilding());
                    room.setCapacity(newRoom.getCapacity());
                    room.setDescription(newRoom.getDescription());
                    room.setOnlyStaff(newRoom.getOnlyStaff());
                    return roomRepository.save(room);
                }).orElseThrow(() -> new RoomNotFoundException(id));
    }

    // Method to filter rooms based on capacity, a room being accessible to students or not, the facilities that
    // should be present (if so their facility ids should be in the facilities array), the building name and a
    // string of the inputted location
    public List<Room> filterRooms(int capacity, boolean onlyStaff, int[] facilities, String building, String location) {
        LocalTime open = LocalTime.now();
        LocalTime closed = LocalTime.now().plusHours(4); //Put as method params later

        int[] roomIds = roomRepository.getAllRoomIds();
        List<Long> resRoomIds = new ArrayList<>();
        int[] buildingIds;
        boolean allFacs = true;
        for (int roomId : roomIds) {
            long nBId = roomRepository.getRoomById(roomId).getBuilding();
            buildingIds = buildingRepository.filterBuildingsOnLocationAndNameAndTime(location, building, open, closed);
            allFacs = false;
            System.out.println(buildingIds.length);
            if (buildingIds.length > 0) {
                for (int buildingId : buildingIds) {
                    allFacs = buildingId == nBId || allFacs;
                }
            }
            if (allFacs) {
                if (facilities.length > 0) {
                    for (long facility : facilities) {
                        allFacs = roomFacilityRepository.filterRooms(roomId, facility).size() != 0 && allFacs;
                    }
                }
            }
            if (allFacs) {
                resRoomIds.add((long) roomId);
            }
        }
        return (resRoomIds.size() > 0 ? roomRepository.filterRooms(capacity, onlyStaff, resRoomIds) : new ArrayList<Room>());
    }

    //Function for adding a new room
//    public void addRoom(int capacity, boolean onlyStaff, int[] facilities, long buildingId, String description) {
//        long newId = roomRepository.findAll().size() > 0 ? roomRepository.getMaxId() + 1 : 0;
//        Room n = new Room(newId,buildingId,capacity,onlyStaff,description);
//        createRoom(n);
//        rfService.createRoomFacilities(newId,facilities);
//    }

    public Room deleteRoom(int id) throws RoomFacilityNotFoundException {
        try {
            Room rf = readRoom(id);
            roomRepository.delete(readRoom(id));
            rfService.deleteRoomFacilities(id, "room");
            return rf;
        } catch (RoomFacilityNotFoundException e) {
            throw new RoomFacilityNotFoundException(id);
        }
    }

    //Function for updating a room
//    public void updateRoom(long id, int capacity, boolean onlyStaff, Set<Facility> facilities, long buildingId, String description) {
//        int[] facilityIds = roomFacilityRepository.getRoomFacilityIdsByRoomId(id);
//        Room n = new Room(id,buildingId,capacity,onlyStaff,description, facilities);
//        n = updateRoom(n, (int) id);
//    }

}
