package nl.tudelft.oopp.group39.event.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EventController.REST_MAPPING)
public class EventController {
    public static final String REST_MAPPING = "/event";

    @Autowired
    private EventService eventService;

    /**
     * GET Endpoint to retrieve all events.
     *
     * @return a list of events {@link Event}.
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listEvents() {
        return RestResponse.create(eventService.listEvents());
    }

    /**
     * POST Endpoint to retrieve an event.
     *
     * @return the created event {@link Event}.
     */
    @PostMapping("")
    public ResponseEntity<RestResponse<Object>> createEvent(@RequestBody Event event) {
        return RestResponse.create(eventService.createEvent(event), null, HttpStatus.CREATED);
    }

    /**
     * GET Endpoint to retrieve an event.
     *
     * @return the requested event {@link Event}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> readEvent(@PathVariable Integer id) {
        try {
            return RestResponse.create(eventService.readEvent(id));
        } catch (Exception e) {
            return RestResponse.create(null, e.getMessage(), HttpStatus.OK);
        }
    }

    /**
     * PUT Endpoint to update an event.
     *
     * @return the updated event {@link Event}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateEvent(
        @PathVariable Integer id,
        @RequestBody Event event
    ) {
        try {
            return RestResponse.create(eventService.updateEvent(id, event));
        } catch (Exception e) {
            return RestResponse.create(null, e.getMessage(), HttpStatus.OK);
        }
    }

    /**
     * DELETE Endpoint to delete am event.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteEmployee(@PathVariable Integer id) {
        eventService.deleteEvent(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
