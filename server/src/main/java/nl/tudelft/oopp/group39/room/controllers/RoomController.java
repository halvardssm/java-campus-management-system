package nl.tudelft.oopp.group39.room.controllers;

import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.room.dao.RoomDao;
import nl.tudelft.oopp.group39.room.dto.RoomDto;
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
public class RoomController extends AbstractController {
    public static final String REST_MAPPING = "/room";

    @Autowired
    private RoomService service;

    /**
     * Method that gets all the parameters from a user, and then lists them.
     * If the parameters are not entered via a get request, then it returns all rooms.
     *
     * @param allParams parameters that is entered by the user.
     * @return filtered list in accordance to the parameters entered.
     * @see RoomDao#roomFilter(Map)
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(
        @RequestParam Map<String, String> allParams
    ) {
        return restHandler((p) -> Utils.listEntityToDto(service.filterRooms(allParams)));
    }

    /**
     * Creates a room with the dto supplied by the curl request.
     *
     * @param newRoom the dto values of the room to be created
     * @return the inserted value converted back to dto
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(@RequestBody RoomDto newRoom) {
        return restHandler(
            HttpStatus.CREATED,
            (p) -> service.createRoom(newRoom.toEntity()).toDto()
        );
    }

    /**
     * GET Endpoint to get a room.
     *
     * @return the requested room
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> service.readRoom(id).toDto());
    }

    /**
     * PUT Endpoint to update a room.
     *
     * @return the updated room
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @RequestBody RoomDto updated,
        @PathVariable Long id
    ) {
        return restHandler((p) -> service.updateRoom(updated.toEntity(), id).toDto());
    }

    /**
     * Delete Endpoint to delete a room.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(@PathVariable Long id) {
        return restHandler((p) -> {
            service.deleteRoom(id);

            return null;
        });
    }
}
