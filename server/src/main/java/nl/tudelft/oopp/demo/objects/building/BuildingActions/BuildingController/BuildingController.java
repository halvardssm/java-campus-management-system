package nl.tudelft.oopp.demo.objects.building.BuildingActions.BuildingController;

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
        int[] buildingIds;
        if(!building.equals("") && !location.equals("")) {
            buildingIds = buildingRepository.filterBuildingsOnLocationAndName(building, location);
        }
        else if(!building.equals("")) {
            buildingIds = buildingRepository.filterBuildingsOnName(building);
        }
        else if(!location.equals("")) {
            buildingIds = buildingRepository.filterBuildingsOnLocation(location);
        }
        else {
            buildingIds = buildingRepository.getAllBuildingIds();
        }
        List<Long> resBuildingIds = new ArrayList<>();
        boolean allFacs = true;
        for (int buildingId : buildingIds) {
            if (roomRepository.getRoomsByBuildingId(buildingId).size() > 0) {
                int maxCapacity = roomRepository.getMaxRoomCapacityByBuildingId(buildingId);
                if (capacity <= maxCapacity) {
                    resBuildingIds.add((long)buildingId);
                }
            }
        }
        List<Building> newB = new ArrayList<>();
        newB = (resBuildingIds.size() > 0 ? buildingRepository.getAllBuildingsByIds(resBuildingIds) : newB);
        return newB;
    }

    public void addBuilding(String name, String location, String description) {
        int newId = (buildingRepository.findAll().size() > 0 ? buildingRepository.getMaxId() + 1 : 0);
//        int newId = buildingRepository.getMaxId() + 1;
        Building n = new Building(newId,name,location,description);
        createBuilding(n);
    }

}
