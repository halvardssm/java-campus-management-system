package nl.tudelft.oopp.group39.reservable.controllers;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.reservable.dto.FoodDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(FoodController.REST_MAPPING)
public class FoodController extends AbstractController {
    public static final String REST_MAPPING = "/food";

    @Autowired
    private FoodService foodService;

    /**
     * GET Endpoint to retrieve all foods.
     *
     * @return a list of foods {@link Food}.
     */
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(@RequestParam Map<String, String> params) {
        return restHandler((p) -> Utils.listEntityToDto(foodService.listFoods(params)));
    }

    /**
     * POST Endpoint to retrieve an food.
     *
     * @return the created food {@link Food}.
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(@RequestBody FoodDto food) {
        return restHandler(
            HttpStatus.CREATED,
            (p) -> foodService.createFood(food.toEntity()).toDto()
        );
    }

    /**
     * GET Endpoint to retrieve an food.
     *
     * @return the requested food {@link Food}.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> foodService.readFood(id).toDto());
    }

    /**
     * PUT Endpoint to update an food.
     *
     * @return the updated food {@link Food}.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @PathVariable Long id,
        @RequestBody Food food
    ) {
        return restHandler((p) -> foodService.updateFood(id, food).toDto());
    }

    /**
     * DELETE Endpoint to delete am food.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(@PathVariable Long id) {
        return restHandler((p) -> {
            foodService.deleteFood(id);

            return null;
        });
    }
}
