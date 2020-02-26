package nl.tudelft.oopp.group39.room.Controller;

import nl.tudelft.oopp.group39.room.Entities.Room;
import nl.tudelft.oopp.group39.room.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService service;

    @GetMapping("")
    public List<Room> ListRooms(@RequestParam(required = false) Integer capacity, @RequestParam(required = false) Boolean onlyStaff, @RequestParam(required = false) int[] facilities, @RequestParam(required = false) String building, @RequestParam(required = false) String location, @RequestParam(required = false) String open, @RequestParam(required = false) String closed) {
        capacity = capacity == null ? 0 : capacity;
        LocalTime nOpen = open == null ? LocalTime.MAX : LocalTime.parse(open);
        LocalTime nClosed = closed == null ? LocalTime.MIN : LocalTime.parse(closed);
        onlyStaff = onlyStaff == null ? false : onlyStaff;
        facilities = facilities == null ? new int[0] : facilities;
        building = building == null ? "" : building;
        location = location == null ? "" : location;
        boolean allEmpty = capacity == 0
                && !onlyStaff
                && Arrays.equals(facilities, new int[0])
                && building.contentEquals("")
                && location.contentEquals("")
                && nOpen.compareTo(LocalTime.MAX) == 0
                && nClosed.compareTo(LocalTime.MIN) == 0;
        return allEmpty ? service.listRooms() : service.filterRooms(capacity, onlyStaff, facilities, building, location, nOpen, nClosed);
    }

    @DeleteMapping("/{id}")
    public void DeleteRoom(@PathVariable int id) {
        service.deleteRoom(id);
    }

    @PostMapping("")
    @ResponseBody
    public Room AddRoom(@RequestBody Room newRoom) {
        return service.createRoom(newRoom);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Room ReadRoom(@PathVariable int id) {
        return service.readRoom(id);
    }

    @PutMapping ("/{id}")
    @ResponseBody
    public Room updateRoom(@RequestBody Room updated, @PathVariable int id) {
        return service.updateRoom(updated, id);
    }
}
