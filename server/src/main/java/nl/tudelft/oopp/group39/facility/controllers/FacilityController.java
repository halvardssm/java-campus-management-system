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

    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listFacilities() {
        return RestResponse.create(service.listFacilities());
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createFacility(@RequestBody Facility facility) {
        return RestResponse.create(service.createFacility(facility), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> readFacility(@PathVariable int id) {
        return RestResponse.create(service.readFacility(id));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateFacility(@RequestBody Facility updated, @PathVariable int id) {
        return RestResponse.create(service.updateFacility(updated, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteFacility(@PathVariable int id) {
        service.deleteFacility(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }

}
