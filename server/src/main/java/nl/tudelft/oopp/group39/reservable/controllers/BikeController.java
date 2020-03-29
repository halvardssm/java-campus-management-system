package nl.tudelft.oopp.group39.reservable.controllers;

import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BikeController.REST_MAPPING)
public class BikeController {
    public static final String REST_MAPPING = "/bike";

    @Autowired
    private BikeService bikeService;

    /**
     * GET Endpoint to retrieve all bikes.
     *
     * @return a list of bikes {@link Bike}.
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listBikes(
        @RequestParam Map<String, String> params
    ) {
        return RestResponse.create(bikeService.listBikes(params));
    }

    /**
     * POST Endpoint to retrieve a bike.
     *
     * @return the created bike {@link Bike}.
     */
    @PostMapping("")
    public ResponseEntity<RestResponse<Object>> createBike(@RequestBody BikeDto bike) {
        try {
            return RestResponse.create(
                bikeService.createBike((Bike) bike.toEntity()).toDto(), null, HttpStatus.CREATED
            );
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * GET Endpoint to retrieve an bike.
     *
     * @return the requested bike {@link Bike}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> readBike(@PathVariable Long id) {
        try {
            return RestResponse.create(bikeService.readBike(id));
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * PUT Endpoint to update an bike.
     *
     * @return the updated bike {@link Bike}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateBike(
        @PathVariable Long id,
        @RequestBody Bike bike
    ) {
        try {
            return RestResponse.create(bikeService.updateBike(id, bike).toDto());
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * DELETE Endpoint to delete a bike.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteBike(@PathVariable Long id) {
        bikeService.deleteBike(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
