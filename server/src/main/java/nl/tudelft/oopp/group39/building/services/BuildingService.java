package nl.tudelft.oopp.group39.building.services;

import java.util.List;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.exceptions.BuildingNotFoundException;
import nl.tudelft.oopp.group39.building.repositories.BuildingRepository;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    /**
     * Doc. TODO Sven
     */

    public List<Building> listBuildings(Map<String,String> params) {
        return buildingDao.buildingFilter(params);
    }

    public Building readBuilding(long id) throws BuildingNotFoundException {
        return buildingRepository.findById(id)
            .orElseThrow(() -> new BuildingNotFoundException(id));
    }

    /**
     * Doc. TODO Sven
     */
    public Building deleteBuilding(Long id) throws BuildingNotFoundException {
        try {
            Building rf = readBuilding(id);
            buildingRepository.delete(readBuilding(id));
            return rf;
        } catch (BuildingNotFoundException e) {
            throw new BuildingNotFoundException(id);
        }
    }

    /**
     * Doc. TODO Sven
     */
    public Building createBuilding(Building newBuilding) {
        return buildingRepository.save(newBuilding);
    }

    /**
     * Doc. TODO Sven
     */
    public Building updateBuilding(Long id, Building newBuilding) throws BuildingNotFoundException {
        return buildingRepository.findById(id)
            .map(building -> {
                newBuilding.setId(id);
                building = newBuilding;
                return buildingRepository.save(building);
            }).orElseThrow(() -> new BuildingNotFoundException(id));
    }

}
