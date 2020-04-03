package nl.tudelft.oopp.group39.building.services;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.building.dao.BuildingDao;
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
    @Autowired
    private BuildingDao buildingDao;

    /**
     * Creates a list of all buildings.
     * @return that list.
     */
    public List<Building> listBuildings(Map<String,String> params) {
        return buildingDao.buildingFilter(params);
    }

    public Building readBuilding(long id) throws BuildingNotFoundException {
        return buildingRepository.findById(id)
            .orElseThrow(() -> new BuildingNotFoundException(id));
    }

    /**
     * Deletes an existing building or throws a BuildingNotFoundException
     * if the building that is to be deleted isn't found.
     * @param id the id of the building.
     * @return nothing.
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
     * Creates a room with the dto supplied by the curl request.
     *
     * @param newBuilding the values of the building to be created
     * @return the inserted value converted back to dto
     * @return the building.
     */
    public Building createBuilding(Building newBuilding) {
        return buildingRepository.save(newBuilding);
    }

    /**
     * Updates an existing building or throws a BuildingNotFoundException
     * if the building that is to be deleted isn't found.
     * @param id the id of the room.
     * @param newBuilding the values of the building to be updated.
     * @return the updated building.
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
