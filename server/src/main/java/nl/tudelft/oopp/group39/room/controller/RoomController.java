package nl.tudelft.oopp.group39.room.controller;

import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(RoomController.REST_MAPPING)
public class RoomController {
    public static final String REST_MAPPING = "/room";

    @Autowired
    private RoomService service;

    @GetMapping("")
    public List<Room> listRooms(@RequestParam(required = false) Integer capacity, @RequestParam(required = false) Boolean onlyStaff, @RequestParam(required = false) int[] facilities, @RequestParam(required = false) String building, @RequestParam(required = false) String location, @RequestParam(required = false) String open, @RequestParam(required = false) String closed) {
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

    @PostMapping("")
    @ResponseBody
    public Room createRoom(@RequestBody Room newRoom) {
        return service.createRoom(newRoom);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Room readRoom(@PathVariable int id) {
        return service.readRoom(id);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Room updateRoom(@RequestBody Room updated, @PathVariable int id) {
        return service.updateRoom(updated, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable int id) {
        service.deleteRoom(id);
    }
}
