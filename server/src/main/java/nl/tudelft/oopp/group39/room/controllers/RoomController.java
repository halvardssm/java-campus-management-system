package nl.tudelft.oopp.group39.room.controllers;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.room.dao.RoomDao;
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

    @Autowired
    private RoomDao roomDao;

    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listRooms(@RequestParam(required = false) Map<String, Object> allParams) {

        List<Room> result = roomDao.roomFilter(allParams);
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
