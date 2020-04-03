package nl.tudelft.oopp.group39.facility.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
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
public class FacilityController {

    public static final String REST_MAPPING = "/facility";

    @Autowired
    private FacilityService service;

    /**
     * Doc. TODO Sven
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listFacilities() {
        return RestResponse.create(service.listFacilities());
    }

    /**
     * Doc. TODO Sven
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createFacility(@RequestBody Facility facility) {
        try {
            return RestResponse.create(service.createFacility(facility), null, HttpStatus.CREATED);
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * Doc. TODO Sven
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> readFacility(@PathVariable Long id) {
        try {
            return RestResponse.create(service.readFacility(id));
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * Doc. TODO Sven
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateFacility(
        @RequestBody Facility updated,
        @PathVariable Long id
    ) {
        try {
            return RestResponse.create(service.updateFacility(updated, id));
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * Doc. TODO Sven
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteFacility(@PathVariable Long id) {
        service.deleteFacility(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }

}
