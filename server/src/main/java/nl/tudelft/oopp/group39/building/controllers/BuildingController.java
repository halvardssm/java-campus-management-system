package nl.tudelft.oopp.group39.building.controllers;

import java.util.Map;
import nl.tudelft.oopp.group39.building.dto.BuildingDto;
import nl.tudelft.oopp.group39.building.services.BuildingService;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
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
@RequestMapping(BuildingController.REST_MAPPING)
public class BuildingController extends AbstractController {
    public static final String REST_MAPPING = "/building";

    public static final String PARAM_CAPACITY = "capacity";
    public static final String PARAM_OPEN = "open";
    public static final String PARAM_CLOSED = "closed";
    public static final String PARAM_BUILDING = "building";
    public static final String PARAM_LOCATION = "location";

    @Autowired
    private BuildingService buildingService;

    /**
     * TODO Sven.
     */
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(@RequestParam Map<String, String> params) {
        return restHandler((p) -> Utils.listEntityToDto(buildingService.listBuildings(params)));
    }

    /**
     * Create building. TODO Sven
     *
     * @param building building
     * @return building
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(@RequestBody BuildingDto building) {
        return restHandler(
            HttpStatus.CREATED,
            (p) -> buildingService.createBuilding(building.toEntity()).toDto()
        );
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> buildingService.readBuilding(id).toDto());
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @RequestBody BuildingDto updated,
        @PathVariable Long id
    ) {
        return restHandler((p) -> buildingService.updateBuilding(id, updated.toEntity()).toDto());
    }

    /**
     * Delete building. TODO Sven
     *
     * @param id id
     * @return nothing
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(@PathVariable Long id) {
        return restHandler((p) -> {
            buildingService.deleteBuilding(id);

            return null;
        });
    }

}
