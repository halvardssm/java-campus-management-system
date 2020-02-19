package nl.tudelft.oopp.demo.objects.building.BuildingActions.BuildingController.BuildingMaps;

import nl.tudelft.oopp.demo.objects.building.Building;
import nl.tudelft.oopp.demo.objects.building.BuildingActions.BuildingController.BuildingController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class BuildingMaps extends BuildingController {

    //To test this in cmd (only after seeding the database! -> look in room/RoomController to see how to seed the database) ->
    // curl localhost:8080/FilterBuildings?capacity={put capacity here}&building={put building here}&location={put location here}
    // for example: curl localhost:8080/FilterBuildings?capacity=0&building=test&location=test
    @GetMapping("FilterBuildings")
    @ResponseBody
    public List<Building> FilterBuildings(@RequestParam int capacity, @RequestParam String building, @RequestParam String location) {
        return filterBuildings(capacity,building,location);
    }

    //To test this in cmd ->
    // curl localhost:8080/addBuilding?building={put building here}&location={put location here}&description={put description here}
    @GetMapping("addBuilding")
    @ResponseBody
    public String AddBuilding(@RequestParam String building, @RequestParam String location, @RequestParam String description) {
        addBuilding(building, location, description);
        return "saved";
    }
}
