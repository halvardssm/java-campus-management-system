package nl.tudelft.oopp.demo.objects.building.BuildingActions;

import nl.tudelft.oopp.demo.objects.building.*;
import nl.tudelft.oopp.demo.objects.building.Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import nl.tudelft.oopp.demo.config.MysqlConfig;
import java.util.List;

@Controller
public class BuildingActions {


    @Autowired
    private BuildingRepository buildingRepository;

    public Building readBuilding(long id) throws BuildingNotFoundException {
        return buildingRepository.findById(id).orElseThrow(() -> new BuildingNotFoundException((int) id));
    }

    public Building createBuilding(Building newBuilding) {
        try {
            Building building = readBuilding((int)newBuilding.getId());
            throw new BuildingExistsException((int)newBuilding.getId());
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
                    building.setOpen(newBuilding.getOpen());
                    building.setClosed(newBuilding.getClosed());
                    return buildingRepository.save(building);
                }).orElseThrow(() -> new BuildingNotFoundException(id));
    }

}
