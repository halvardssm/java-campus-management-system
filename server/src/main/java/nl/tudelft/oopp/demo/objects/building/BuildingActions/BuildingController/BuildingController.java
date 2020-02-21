package nl.tudelft.oopp.demo.objects.building.BuildingActions.BuildingController;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.objects.building.*;
import nl.tudelft.oopp.demo.objects.building.BuildingActions.BuildingActions;
import nl.tudelft.oopp.demo.objects.room.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BuildingController extends BuildingActions {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    public List<Building> filterBuildings(int capacity, String building, String location) {
        LocalTime open = LocalTime.now().plusHours(2);
        LocalTime closed = LocalTime.now().minusHours(2);
//        int[] buildingIds = buildingRepository.filterBuildingsOnLocationAndName(building, location);
        int[] buildingIds = buildingRepository.filterBuildingsOnLocationAndNameAndTime(building, location, open, closed);

        List<Long> resBuildingIds = new ArrayList<>();
        for (int buildingId : buildingIds) {
            if (roomRepository.getRoomsByBuildingId(buildingId).size() > 0) {
                int maxCapacity = roomRepository.getMaxRoomCapacityByBuildingId(buildingId);
                if (capacity <= maxCapacity) {
                    resBuildingIds.add((long)buildingId);
                }
            }
        }
        return (resBuildingIds.size() > 0 ? buildingRepository.getAllBuildingsByIds(resBuildingIds) : new ArrayList<Building>());
    }

    public void addBuilding(String name, String location, String description){//, LocalTime open, LocalTime closed) {
        LocalTime open = LocalTime.now().minusHours(4);
        LocalTime closed = LocalTime.now().plusHours(4);
        int newId = (buildingRepository.findAll().size() > 0 ? buildingRepository.getMaxId() + 1 : 0);
        Building n = new Building(newId,name,location,description, open, closed);
        createBuilding(n);
    }

}
