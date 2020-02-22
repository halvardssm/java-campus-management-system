package nl.tudelft.oopp.demo.objects.room.Service;

import nl.tudelft.oopp.demo.objects.building.Repositories.BuildingRepository;
import nl.tudelft.oopp.demo.objects.room.Exceptions.*;
import nl.tudelft.oopp.demo.objects.room.Entities.Room;
import nl.tudelft.oopp.demo.objects.room.repositories.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.Exceptions.RoomFacilityNotFoundException;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacility;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityActions.RoomFacilityActions;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityRepository;
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
    private RoomFacilityActions rfService;

    public Room readRoom(long id) throws RoomNotFoundException {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException((int)id));
    }

    public List<Room> listRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room newRoom) {
        try {
            Room room = readRoom((int)newRoom.getId());
            throw new RoomExistsException((int)newRoom.getId());
        }
        catch (RoomNotFoundException e){
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
            buildingIds = buildingRepository.filterBuildingsOnLocationAndNameAndTime(location,building,open,closed);
            allFacs = false;
            System.out.println(buildingIds.length);
            if (buildingIds.length > 0) {
                for (int buildingId : buildingIds) {
                    allFacs =  buildingId == nBId? true : allFacs;
                }
            }
            if (allFacs) {
                if (facilities.length > 0) {
                    for (long facility : facilities) {
                        allFacs = roomFacilityRepository.filterRooms((long)roomId, facility).size() == 0 ? false : allFacs;
                    }
                }
            }
            if (allFacs) {
                resRoomIds.add((long)roomId);
            }
        }
        return (resRoomIds.size() > 0 ? roomRepository.filterRooms(capacity, onlyStaff, resRoomIds) : new ArrayList<Room>());
    }

    //Function for adding a new room
    public void addRoom(int capacity, boolean onlyStaff, int[] facilities, long buildingId, String description) {
        long newId = roomRepository.findAll().size() > 0 ? roomRepository.getMaxId() + 1 : 0;
        Room n = new Room(newId,buildingId,capacity,onlyStaff,description);
        createRoom(n);
        createRoomFacilities(newId,facilities);
    }

    public Room deleteRoom(int id) throws RoomFacilityNotFoundException{
        try {
            Room rf = readRoom((long) id);
            roomRepository.delete(readRoom((long) id));
            return rf;
        }
        catch (RoomFacilityNotFoundException e) {
            throw new RoomFacilityNotFoundException(id);
        }
    }

    //Function for updating a room
    public void updateRoom(long id, int capacity, boolean onlyStaff, int[] facilities, long buildingId, String description) {
        int[] facilityIds = roomFacilityRepository.getRoomFacilityIdsByRoomId(id);
        Room n = new Room(id,buildingId,capacity,onlyStaff,description);
        n = updateRoom(n, (int) id);
        for (int facility : facilityIds) {
            rfService.deleteRoomFacility(facility);
        }
        createRoomFacilities(id,facilities);
    }

    public void createRoomFacilities(long newId,int[] facilities) {
        for (int facility : facilities) {
            long newFId = roomFacilityRepository.findAll().size() > 0 ? roomFacilityRepository.getMaxId() + 1 : 0;
            RoomFacility f = new RoomFacility(newFId, newId, facility);
            rfService.createRoomFacility(f);
        }
    }

    public void deleteRoomFacilities(long id,int[] facilities) {
        int[] facilityIds = roomFacilityRepository.getRoomFacilityIdsByRoomId(id);
        for (int facility : facilityIds) {
            rfService.deleteRoomFacility(facility);
        }
    }

}
