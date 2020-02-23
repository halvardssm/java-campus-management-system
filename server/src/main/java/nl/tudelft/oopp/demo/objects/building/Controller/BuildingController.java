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

    @GetMapping("")
    public List<Building> ListBuildings() {
        return service.listBuildings();
    }

    @DeleteMapping("/{id}")
    public void DeleteBuilding(@RequestParam int id) {
        service.deleteBuilding((long) id);
    }

    @PostMapping("")
    @ResponseBody
    public String AddBuilding(@RequestBody Building building){//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        service.addBuilding(building.getName(), building.getLocation(), building.getDescription());//, open, closed);
        return "saved";
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<Building> FilterBuildings(@RequestParam int capacity, @RequestParam String building, @RequestParam String location){//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        return service.filterBuildings(capacity,building,location);//, open, closed);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Building ReadBuilding(@RequestParam int id) {
        return service.readBuilding((long) id);
    }

    @PutMapping ("/{id}")
    @ResponseBody
    public Building updateBuilding(@RequestBody Building updated, @PathVariable int id) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed)
        return service.updateBuilding(updated, id);
    }

}
