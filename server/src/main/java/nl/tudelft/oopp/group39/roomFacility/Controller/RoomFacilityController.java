package nl.tudelft.oopp.group39.roomFacility.Controller;


import nl.tudelft.oopp.group39.roomFacility.Entities.RoomFacility;
import nl.tudelft.oopp.group39.roomFacility.RoomFacilityService.RoomFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomfacility")
public class RoomFacilityController {

    @Autowired
    private RoomFacilityService service;

    @GetMapping("")
    public List<RoomFacility> ListRoomFacilities() {
        return service.listRoomFacilities();
    }

    @DeleteMapping("/{id}")
    public void DeleteRoomFacility(@PathVariable int id) {
        service.deleteRoomFacility(id);
    }

    @PostMapping("")
    @ResponseBody
    public String AddRoom(@RequestBody RoomFacility newRoomFacility) {
        int[] facilities = new int[0];
        service.addRoomFacility((int)newRoomFacility.getRoomId(),(int)newRoomFacility.getFacilityId());
        return "saved";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public RoomFacility ReadRoomFacility(@PathVariable int id) {
        return service.readRoomFacility(id);
    }

    @PutMapping ("/{id}")
    @ResponseBody
    public RoomFacility updateRoomFacility(@RequestBody RoomFacility updated, @PathVariable int id) {
        return service.updateRoomFacility(updated, id);
    }

    //long id, int capacity, boolean onlyStaff, int[] facilities, long buildingId, String description
}
