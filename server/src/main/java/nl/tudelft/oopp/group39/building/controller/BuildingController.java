package nl.tudelft.oopp.group39.building.controller;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BuildingController.REST_MAPPING)
public class BuildingController {
    public static final String REST_MAPPING = "/building";

    public static final String PARAM_CAPACITY = "capacity";
    public static final String PARAM_OPEN = "open";
    public static final String PARAM_CLOSED = "closed";
    public static final String PARAM_BUILDING = "building";
    public static final String PARAM_LOCATION = "location";

    @Autowired
    private BuildingService buildingService;

    @GetMapping("")
    public List<Building> listBuildings(@RequestParam(required = false) Integer capacity, @RequestParam(required = false) String building, @RequestParam(required = false) String location, @RequestParam(required = false) String open, @RequestParam(required = false) String closed) {
        capacity = capacity == null ? 0 : capacity;
        LocalTime nOpen = open == null ? LocalTime.MIN : LocalTime.parse(open);
        LocalTime nClosed = closed == null ? LocalTime.MAX : LocalTime.parse(closed);
        building = building == null ? "" : building;
        location = location == null ? "" : location;
        boolean allEmpty = capacity == 0
                && building.contentEquals("")
                && location.contentEquals("")
                && nOpen.compareTo(LocalTime.MIN) == 0 //opening time is equal to 23:59
                && nClosed.compareTo(LocalTime.MAX) == 0;
        return allEmpty ? buildingService.listBuildings() : buildingService.filterBuildings(capacity, building, location, nOpen, nClosed);
    }

    @PostMapping("")
    @ResponseBody
    public String createBuilding(@RequestBody Building building) {
        buildingService.createBuilding(building);//, open, closed);
        return "saved";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Building readBuilding(@PathVariable int id) {
        return buildingService.readBuilding(id);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Building updateBuilding(@RequestBody Building updated, @PathVariable int id) {
        return buildingService.updateBuilding(updated, id);
    }

    @DeleteMapping("/{id}")
    public void deleteBuilding(@PathVariable int id) {
        buildingService.deleteBuilding(id);
    }

}
