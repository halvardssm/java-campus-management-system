package nl.tudelft.oopp.group39.event.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.services.EventService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EventController.REST_MAPPING)
public class EventController extends AbstractController {
    public static final String REST_MAPPING = "/event";

    @Autowired
    private EventService eventService;

    /**
     * GET Endpoint to retrieve all events.
     *
     * @return a list of events {@link Event}.
     */
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list() {
        return restHandler((p) -> eventService.listEvents());
    }

    /**
     * POST Endpoint to create event.
     *
     * @return the created event {@link Event}.
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(@RequestBody Event event) {
        return restHandler(HttpStatus.CREATED, (p) -> eventService.createEvent(event));
    }

    /**
     * GET Endpoint to retrieve event.
     *
     * @return the requested event {@link Event}.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> eventService.readEvent(id));
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
        @RequestBody Event event
    ) {
        return restHandler((p) -> eventService.updateEvent(id, event));
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
