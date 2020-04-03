package nl.tudelft.oopp.group39.room.controllers;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.room.dao.RoomDao;
import nl.tudelft.oopp.group39.room.dto.RoomDto;
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
     * Method that gets all the parameters from a user, and then lists them.
     * If the parameters are not entered via a get request, then it returns all rooms.
     *
     * @param allParams parameters that is entered by the user.
     * @return filtered list in accordance to the parameters entered.
     *
     * @see RoomDao#roomFilter(Map)
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listRooms(
        @RequestParam Map<String, String> allParams
    ) {
        List<Room> roomList = service.filterRooms(allParams);

        return RestResponse.create(Utils.listEntityToDto(roomList));
    }

    /**
     * Creates a room with the dto supplied by the curl request.
     *
     * @param newRoom the dto values of the room to be created
     * @return the inserted value converted back to dto
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createRoom(@RequestBody RoomDto newRoom) {
        return RestResponse.create(
            service.createRoom(newRoom.toEntity()).toDto(),
            null,
            HttpStatus.CREATED
        );
    }

    /**
     * Gets/Reads an existing room or throws a RoomNotFoundException.
     * @param id the id of the room.
     * @return the room.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> readRoom(@PathVariable Long id) {
        return RestResponse.create(service.readRoom(id).toDto());
    }

    /**
     * Updates an existing room or throws a RoomNotFoundException.
     * @param id the id of the room.
     * @param updated the dto values of the room to be updated.
     * @return the updated room.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateRoom(
        @RequestBody RoomDto updated,
        @PathVariable Long id
    ) {
        return RestResponse.create(service.updateRoom(updated.toEntity(), id).toDto());
    }

    /**
     * Deletes an existing room or throws a RoomNotFoundException.
     * @param id the id of the room.
     * @return nothing.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteRoom(@PathVariable Long id) {
        service.deleteRoom(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
