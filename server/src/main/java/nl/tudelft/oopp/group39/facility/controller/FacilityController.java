package nl.tudelft.oopp.group39.facility.controller;

import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facility")
public class FacilityController {

    @Autowired
    private FacilityService service;

    @GetMapping("")
    public List<Facility> listFacilities() {
        return service.listFacilities();
    }

    @PostMapping("")
    @ResponseBody
    public Facility addFacility(@RequestBody Facility facility) {
        return service.createFacility(facility);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Facility readFacility(@PathVariable int id) {
        return service.readFacility(id);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Facility updateFacility(@RequestBody Facility updated, @PathVariable int id) {
        return service.updateFacility(updated, id);
    }

    @DeleteMapping("/{id}")
    public void deleteFacility(@PathVariable int id) {
        service.deleteFacility(id);
    }

}
