package nl.tudelft.oopp.group39.reservable.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.reservable.entities.Food;
import nl.tudelft.oopp.group39.reservable.services.FoodService;
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
@RequestMapping(FoodController.REST_MAPPING)
public class FoodController {
    public static final String REST_MAPPING = "/food";

    @Autowired
    private FoodService foodService;

    /**
     * GET Endpoint to retrieve all foods.
     *
     * @return a list of foods {@link Food}.
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listFoods() {
        return RestResponse.create(foodService.listFoods());
    }

    /**
     * POST Endpoint to retrieve an food.
     *
     * @return the created food {@link Food}.
     */
    @PostMapping("")
    public ResponseEntity<RestResponse<Object>> createFood(@RequestBody Food food) {
        try {
            return RestResponse.create(foodService.createFood(food), null, HttpStatus.CREATED);
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * GET Endpoint to retrieve an food.
     *
     * @return the requested food {@link Food}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> readFood(@PathVariable Integer id) {
        try {
            return RestResponse.create(foodService.readFood(id));
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * PUT Endpoint to update an food.
     *
     * @return the updated food {@link Food}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateFood(
        @PathVariable Integer id,
        @RequestBody Food food
    ) {
        try {
            return RestResponse.create(foodService.updateFood(id, food));
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * DELETE Endpoint to delete am food.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteFood(@PathVariable Integer id) {
        foodService.deleteFood(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
