package nl.tudelft.oopp.demo.objects.facility.Controller;

import nl.tudelft.oopp.demo.objects.facility.Entities.Facility;
import nl.tudelft.oopp.demo.objects.facility.FacilityActions.FacilityService;
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

    @DeleteMapping("/{id}")
    public void DeleteFacility(@PathVariable int id) {
        service.deleteFacility(id);
    }

    @PostMapping("")
    @ResponseBody
    public String AddFacility(@RequestBody Facility facility){//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        service.addFacility(facility.getDescription());//, open, closed);
        return "saved";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Facility ReadFacility(@PathVariable int id) {
        return service.readFacility((long) id);
    }

    @PutMapping ("/{id}")
    @ResponseBody
    public Facility updateFacility(@RequestBody Facility updated, @PathVariable int id) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed)
        return service.updateFacility(updated, id);
    }

}
