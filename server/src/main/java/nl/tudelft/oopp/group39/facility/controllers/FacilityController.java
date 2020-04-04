package nl.tudelft.oopp.group39.facility.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.services.FacilityService;
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
@RequestMapping(FacilityController.REST_MAPPING)
public class FacilityController extends AbstractController {

    public static final String REST_MAPPING = "/facility";

    @Autowired
    private FacilityService service;

    /**
     * GET endpoint to retrieve all facilities.
     *
     * @return a list of facilities
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list() {
        return restHandler((p) -> service.listFacilities());
    }

    /**
     * POST endpoint to create a facility.
     *
     * @return the created facility
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(@RequestBody Facility facility) {
        return restHandler(HttpStatus.CREATED, (p) -> service.createFacility(facility));
    }

    /**
     * GET endpoint to retrieve the facility.
     *
     * @return the requested facility
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> service.readFacility(id));
    }

    /**
     * PUT endpoint to update the facility.
     *
     * @return the updated facility
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @RequestBody Facility updated,
        @PathVariable Long id
    ) {
        return restHandler((p) -> service.updateFacility(updated, id));
    }

    /**
     * DELETE endpoint to delete the facility.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(@PathVariable Long id) {
        return restHandler((p) -> {
            service.deleteFacility(id);

            return null;
        });
    }
}
