package nl.tudelft.oopp.group39.room.controllers;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
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
@RequestMapping(RoomController.REST_MAPPING)
public class RoomController {
    public static final String REST_MAPPING = "/room";

    @Autowired
    private RoomService service;

    /**
     * Doc. TODO Sven
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listRooms(
        @RequestParam(required = false) Integer capacity,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Boolean onlyStaff,
        @RequestParam(required = false) int[] facilities,
        @RequestParam(required = false) String building,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String open,
        @RequestParam(required = false) String closed
    ) {
        capacity = capacity == null ? 0 : capacity;
        name = name == null ? "" : name;
        LocalTime nOpen = open == null ? LocalTime.MAX : LocalTime.parse(open);
        LocalTime nClosed = closed == null ? LocalTime.MIN : LocalTime.parse(closed);
        onlyStaff = onlyStaff == null ? false : onlyStaff;
        facilities = facilities == null ? new int[0] : facilities;
        building = building == null ? "" : building;
        location = location == null ? "" : location;
        LocalTime newOpen = open == null ? LocalTime.MAX : LocalTime.parse(open);
        LocalTime newClosed = closed == null ? LocalTime.MIN : LocalTime.parse(closed);
        boolean allEmpty = capacity == 0
                && name.contentEquals("")
            && !onlyStaff
            && Arrays.equals(facilities, new int[0])
            && building.contentEquals("")
            && location.contentEquals("")
            && newOpen.compareTo(LocalTime.MAX) == 0
            && newClosed.compareTo(LocalTime.MIN) == 0;

        List<Room> result = allEmpty
            ? service.listRooms()
            : service.filterRooms(
            capacity,
            onlyStaff,
            facilities,
            building,
            location,
            newOpen,
            newClosed
        );
        return RestResponse.create(result);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createRoom(@RequestBody Room newRoom) {
        return RestResponse.create(service.createRoom(newRoom), null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateRoom(
        @RequestBody Room updated,
        @PathVariable int id
    ) {
        return RestResponse.create(service.updateRoom(updated, id));
    }

    /**
     * Doc. TODO Sven
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteRoom(@PathVariable int id) {
        service.deleteRoom(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
