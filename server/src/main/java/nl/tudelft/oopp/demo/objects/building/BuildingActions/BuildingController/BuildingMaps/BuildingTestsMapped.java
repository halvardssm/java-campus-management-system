package nl.tudelft.oopp.demo.objects.building.BuildingActions.BuildingController.BuildingMaps;

import nl.tudelft.oopp.demo.objects.building.*;
import nl.tudelft.oopp.demo.objects.building.BuildingActions.BuildingController.BuildingController;
import nl.tudelft.oopp.demo.objects.room.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BuildingTestsMapped extends BuildingController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("ab")
    @ResponseBody
    public String nBuilding() {
        int[] facilities = {0,6,9};
        addBuilding("drebbel", "tu", "testAdd");
        return "saved";
    }
    @GetMapping("bfr")
    @ResponseBody
    public List<Building> BuildingFilterRes() {
        return filterBuildings(0,"","");
    }
    @GetMapping("bf")
    @ResponseBody
    public List<Building> BuildingFilter() {
        return filterBuildings(0,"test","test");
    }
}
