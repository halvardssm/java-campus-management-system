package nl.tudelft.oopp.group39.building.controller;

import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private BuildingService service;

    @GetMapping("")
    public List<Building> ListBuildings(@RequestParam(required = false) Integer capacity, @RequestParam(required = false) String building, @RequestParam(required = false) String location, @RequestParam(required = false) String open, @RequestParam(required = false) String closed) {
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
        return allEmpty ? service.listBuildings() : service.filterBuildings(capacity, building, location, nOpen, nClosed);
    }

    @DeleteMapping("/{id}")
    public void DeleteBuilding(@PathVariable int id) {
        service.deleteBuilding(id);
    }

    @PostMapping("")
    @ResponseBody
    public String AddBuilding(@RequestBody Building building) {
        service.createBuilding(building);
        return "saved";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Building ReadBuilding(@PathVariable int id) {
        return service.readBuilding(id);
    }

    @PutMapping ("/{id}")
    @ResponseBody
    public Building updateBuilding(@RequestBody Building updated, @PathVariable int id) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed)
        return service.updateBuilding(updated, id);
    }

}
