package nl.tudelft.oopp.demo.objects.room.RoomActions.RoomController;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.objects.room.Room;
import nl.tudelft.oopp.demo.objects.room.RoomActions.RoomActions;
import nl.tudelft.oopp.demo.objects.building.*;
import nl.tudelft.oopp.demo.objects.room.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

//@EnableJpaRepositories(basePackages = {
//        "nl.tudelft.oopp.demo.repositories",
//        "nl.tudelft.oopp.demo.objects.building",
//        "nl.tudelft.oopp.demo.objects.room",
//        "nl.tudelft.oopp.demo.objects.roomFacility"
//})
@Controller
public class RoomController extends RoomActions{

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    // Method to filter rooms based on capacity, a room being accessible to students or not, the facilities that
    // should be present (if so their facility ids should be in the facilities array), the building name and a
    // string of the inputted location
    public List<Room> filterRooms(int capacity, boolean onlyStaff, int[] facilities, String building, String location) {
        int[] roomIds = roomRepository.getAllRoomIds();
        List<Long> resRoomIds = new ArrayList<>();
        int[] buildingIds;
        boolean allFacs = true;
        for (int roomId : roomIds) {
            long nBId = roomRepository.getRoomById(roomId).getBuilding();
            buildingIds = buildingRepository.filterBuildingsOnLocationAndName(location,building);
            allFacs = false;
            System.out.println(buildingIds.length);
            if (buildingIds.length > 0) {
                for (int buildingId : buildingIds) {
                    allFacs =  (buildingId == nBId? true : allFacs);
                }
            }
            if (allFacs) {
                if (facilities.length > 0) {
                    for (long facility : facilities) {
                        allFacs = (roomFacilityRepository.filterRooms((long)roomId, facility).size() == 0 ? false : allFacs);
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
        long newId = (roomRepository.findAll().size() > 0 ? roomRepository.getMaxId() + 1 : 0);
        Room n = new Room(newId,buildingId,capacity,onlyStaff,description);
        createRoom(n);
        for (int facility : facilities) {
            long newFId = (roomFacilityRepository.findAll().size() > 0 ? roomFacilityRepository.getMaxId() + 1 : 0);
            RoomFacility f = new RoomFacility(newFId, newId, facility);
            roomFacilityRepository.save(f);
        }
    }

}
