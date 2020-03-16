package nl.tudelft.oopp.group39.room.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RestResponse<Object>> listRooms(@RequestParam(required = false) Integer capacity, @RequestParam(required = false) Boolean onlyStaff, @RequestParam(required = false) int[] facilities, @RequestParam(required = false) String building, @RequestParam(required = false) String location, @RequestParam(required = false) String open, @RequestParam(required = false) String closed) {
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

        List<Room> result = allEmpty ? service.listRooms() : service.filterRooms(capacity, onlyStaff, facilities, building, location, nOpen, nClosed);
        return RestResponse.create(result);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createRoom(@RequestBody Room newRoom) {
        return RestResponse.create(service.createRoom(newRoom), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> readRoom(@PathVariable int id) {
        return RestResponse.create(service.readRoom(id));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateRoom(@RequestBody Room updated, @PathVariable int id) {
        return RestResponse.create(service.updateRoom(updated, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteRoom(@PathVariable int id) {
        service.deleteRoom(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
