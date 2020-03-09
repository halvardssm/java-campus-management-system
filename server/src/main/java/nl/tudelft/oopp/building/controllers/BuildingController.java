package nl.tudelft.oopp.building.controllers;

import java.time.LocalTime;
import java.util.List;
import nl.tudelft.oopp.building.entities.Building;
import nl.tudelft.oopp.building.services.BuildingService;
import nl.tudelft.oopp.config.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RestResponse<Object>> listBuildings(@RequestParam(required = false) Integer capacity, @RequestParam(required = false) String building, @RequestParam(required = false) String location, @RequestParam(required = false) String open, @RequestParam(required = false) String closed) {
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
        List<Building> result = allEmpty ? buildingService.listBuildings() : buildingService.filterBuildings(capacity, building, location, nOpen, nClosed);

        return RestResponse.create(result);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createBuilding(@RequestBody Building building) {
        return RestResponse.create(buildingService.createBuilding(building), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> readBuilding(@PathVariable int id) {
        return RestResponse.create(buildingService.readBuilding(id));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateBuilding(@RequestBody Building updated, @PathVariable int id) {
        return RestResponse.create(buildingService.updateBuilding(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteBuilding(@PathVariable int id) {
        buildingService.deleteBuilding(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }

}
