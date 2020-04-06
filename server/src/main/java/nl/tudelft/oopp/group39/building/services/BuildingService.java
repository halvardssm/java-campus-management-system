package nl.tudelft.oopp.group39.building.services;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.building.dao.BuildingDao;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.repositories.BuildingRepository;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private BuildingDao buildingDao;

    /**
     * List all buildings.
     *
     * @return a list of buildings
     */
    public List<Building> listBuildings(Map<String, String> params) {
        return buildingDao.buildingFilter(params);
    }

    /**
     * Reads the building inside the database using its id.
     *
     * @param id the id of the building
     * @return the Building that was found.
     * @throws NotFoundException when no building is found.
     */
    public Building readBuilding(Long id) throws NotFoundException {
        return buildingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Building.MAPPED_NAME, id));
    }

    /**
     * Deletes a building.
     *
     * @param id the id of the building
     * @throws NotFoundException if the building wasn't found
     */
    public Building deleteBuilding(Long id) throws NotFoundException {
        try {
            Building rf = readBuilding(id);
            buildingRepository.delete(readBuilding(id));
            return rf;
        } catch (NotFoundException e) {
            throw new NotFoundException(Building.MAPPED_NAME, id);
        }
    }

    /**
     * Creates a building.
     *
     * @param newBuilding the new building that you want to create
     * @return the created building
     */
    public Building createBuilding(Building newBuilding) {
        return buildingRepository.save(newBuilding);
    }

    /**
     * Updates a building.
     *
     * @param id          the id of the building that you want to update
     * @param newBuilding the new building
     * @return the updated booking
     * @throws NotFoundException if the building wasn't found
     */
    public Building updateBuilding(Long id, Building newBuilding) throws NotFoundException {
        return buildingRepository.findById(id)
            .map(building -> {
                building.setName(newBuilding.getName());
                building.setDescription(newBuilding.getDescription());
                building.setLocation(newBuilding.getLocation());
                building.setOpen(newBuilding.getOpen());
                building.setClosed(newBuilding.getClosed());

                if (newBuilding.getImage() != null) {
                    building.setImage(newBuilding.getImage());
                }

                return buildingRepository.save(building);
            }).orElseThrow(() -> new NotFoundException(Building.MAPPED_NAME, id));
    }
}
