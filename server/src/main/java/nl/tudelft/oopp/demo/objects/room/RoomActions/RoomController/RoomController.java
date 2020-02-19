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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        List<Integer> resRoomIds = new ArrayList<>();
        int[] buildingIds;
        boolean allFacs = true;
        for (int roomId : roomIds) {
            if (!building.equals("")) {
                buildingIds = buildingRepository.filterBuildingsOnName(building);
                allFacs = false;
                if (buildingIds.length > 0) {
                    for (int buildingId : buildingIds) {
                        if (buildingId == roomRepository.getRoomById(roomId).getBuilding()) {
                            allFacs = true;
                        }
                    }
                }
            }
            if (allFacs) {
                if (!location.equals("")) {
                    buildingIds = buildingRepository.filterBuildingsOnLocation(location);
                    allFacs = false;
                    if (buildingIds.length > 0) {
                        for (int buildingId : buildingIds) {
                            if (buildingId == roomRepository.getRoomById(roomId).getBuilding()) {
                                allFacs = true;
                            }
                        }
                    }
                }
                if (facilities.length > 0) {
                    for (int facility : facilities) {
                        if (roomFacilityRepository.filterRooms(roomId, facility).size() == 0) {
                            allFacs = false;
                        }
                    }
                }
            }
            if (allFacs) {
                resRoomIds.add(roomId);
            }
        }
        return roomRepository.filterRooms(11, true, resRoomIds);
    }

    //Function for adding a new room
    public void addRoom(int capacity, boolean onlyStaff, int[] facilities, int buildingId, String description) {
        int newId = roomRepository.getMaxId() + 1;
        Room n = new Room(newId,buildingId,capacity,onlyStaff,description);
        createRoom(n);
        for (int facility : facilities) {
            int newFId = roomFacilityRepository.getMaxId() + 1;
            RoomFacility f = new RoomFacility(newFId, newId, facility);
            roomFacilityRepository.save(f);
        }
    }

}
