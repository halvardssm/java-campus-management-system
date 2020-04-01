package nl.tudelft.oopp.group39.reservable.controllers;

import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.reservable.dto.BikeDto;
import nl.tudelft.oopp.group39.reservable.entities.Bike;
import nl.tudelft.oopp.group39.reservable.services.BikeService;
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
@RequestMapping(BikeController.REST_MAPPING)
public class BikeController extends AbstractController {
    public static final String REST_MAPPING = "/bike";

    @Autowired
    private BikeService bikeService;

    /**
     * GET Endpoint to retrieve all bikes.
     *
     * @return a list of bikes {@link Bike}.
     */
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(@RequestParam Map<String, String> params) {
        return restHandler((p) -> bikeService.listBikes(params));
    }

    /**
     * POST Endpoint to retrieve a bike.
     *
     * @return the created bike {@link Bike}.
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(@RequestBody BikeDto bike) {
        return restHandler(
            HttpStatus.CREATED,
            (p) -> bikeService.createBike(bike.toEntity()).toDto()
        );
    }

    /**
     * GET Endpoint to retrieve an bike.
     *
     * @return the requested bike {@link Bike}.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> bikeService.readBike(id));
    }

    /**
     * PUT Endpoint to update an bike.
     *
     * @return the updated bike {@link Bike}.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @PathVariable Long id,
        @RequestBody Bike bike
    ) {
        return restHandler((p) -> bikeService.updateBike(id, bike).toDto());
    }

    /**
     * DELETE Endpoint to delete a bike.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(@PathVariable Long id) {
        return restHandler((p) -> {
            bikeService.deleteBike(id);

            return null;
        });
    }
}
