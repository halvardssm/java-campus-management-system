package nl.tudelft.oopp.demo.objects.building.Controller;

import java.time.LocalTime;
import java.util.List;

import nl.tudelft.oopp.demo.objects.building.Entities.Building;
import nl.tudelft.oopp.demo.objects.building.Service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/building")
public class BuildingController {

    @Autowired
    private BuildingService service;

    @GetMapping("/list")
    public List<Building> ListBuildings() {
        return service.listBuildings();
    }

    @GetMapping("/delete")
    public void DeleteBuilding(@RequestParam int id) {
        service.deleteBuilding((long) id);
    }

    @GetMapping("/add")
    @ResponseBody
    public String AddBuilding(@RequestParam String building, @RequestParam String location, @RequestParam String description){//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        service.addBuilding(building, location, description);//, open, closed);
        return "saved";
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<Building> FilterBuildings(@RequestParam int capacity, @RequestParam String building, @RequestParam String location){//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        return service.filterBuildings(capacity,building,location);//, open, closed);
    }

    @GetMapping("/read")
    @ResponseBody
    public Building ReadBuilding(@RequestParam int id) {
        return service.readBuilding((long) id);
    }

    @GetMapping ("/update")
    @ResponseBody
    public Building updateBuilding(@RequestParam String building, @RequestParam String location, @RequestParam String description, @RequestParam int id) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        LocalTime open = LocalTime.now();
        Building updated = new Building((long)id, building, location, description, open, open);
        return service.updateBuilding(updated, id);
    }

}
