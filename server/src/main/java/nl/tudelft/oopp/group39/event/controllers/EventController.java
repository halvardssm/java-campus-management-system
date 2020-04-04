package nl.tudelft.oopp.group39.event.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.event.dto.EventDto;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.services.EventService;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
import nl.tudelft.oopp.group39.user.services.UserService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EventController.REST_MAPPING)
public class EventController extends AbstractController {
    public static final String REST_MAPPING = "/event";

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    /**
     * GET Endpoint to retrieve all events.
     *
     * @return a list of events {@link Event}.
     */
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(
        @RequestParam Map<String, String> params
    ) {
        return restHandler((p) -> Utils.listEntityToDto(eventService.listEvents(params)));
    }

    /**
     * POST Endpoint to create event.
     *
     * @return the created event {@link Event}.
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(@RequestBody EventDto event) {
        return restHandler(HttpStatus.CREATED, (p) -> {
            Event event1 = event.toEntity();
            event1.setUser(userService.readUser(event.getUser()));
            return eventService.createEvent(event1).toDto();
        });
    }

    /**
     * GET Endpoint to retrieve event.
     *
     * @return the requested event {@link Event}.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> eventService.readEvent(id).toDto());
    }

    /**
     * PUT Endpoint to update event.
     *
     * @return the updated event {@link Event}.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @PathVariable Long id,
        @RequestBody EventDto event
    ) {
        return restHandler((p) -> {
            Set<Room> rooms = new HashSet<>();

            if (event.getRooms() != null && event.getRooms().size() > 0) {
                Map<String, String> roomMap = new HashMap<>();

                roomMap.put(
                    Room.COL_ID,
                    String.join(
                        ",",
                        event.getRooms().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))
                    )
                );

                rooms.addAll(roomService.filterRooms(roomMap));
            }

            Event event1 = event.toEntity();

            event1.setUser(
                event.getUser() != null
                    ? userService.readUser(event.getUser())
                    : null
            );
            event1.setRooms(rooms);

            return eventService.updateEvent(id, event1).toDto();
        });
    }

    /**
     * DELETE Endpoint to delete event.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(@PathVariable Long id) {
        return restHandler((p) -> {
            eventService.deleteEvent(id);

            return null;
        });
    }
}
