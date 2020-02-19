package nl.tudelft.oopp.demo.objects.building.BuildingActions;

import nl.tudelft.oopp.demo.objects.building.*;
import nl.tudelft.oopp.demo.objects.building.Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BuildingActions {


    @Autowired
    private BuildingRepository buildingRepository;

    public Building readBuilding(int id) throws BuildingNotFoundException {
        List<Building> buildings = buildingRepository.findById(id);
        if(buildings.size() > 0) {
            return buildings.get(0);
        }
        throw new BuildingNotFoundException((int) id);
    }

    public Building createBuilding(Building newBuilding) {
        try {
            Building building = readBuilding(newBuilding.getId());
            throw new BuildingExistsException(newBuilding.getId());
        }
        catch (BuildingNotFoundException e){
            buildingRepository.save(newBuilding);
            return newBuilding;

        }
    }

    public Building updateBuilding(Building newBuilding, int id) throws BuildingNotFoundException {
        return buildingRepository.findById((long) id) //Test if this is the problem, create own method
                .map(building -> {
                    building.setName(newBuilding.getName());
                    building.setLocation(newBuilding.getLocation());
                    building.setDescription(newBuilding.getDescription());
                    return buildingRepository.save(building);
                }).orElseThrow(() -> new BuildingNotFoundException(id));
    }

}
