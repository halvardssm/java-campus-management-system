package nl.tudelft.oopp.group39.building.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.exceptions.BuildingExistsException;
import nl.tudelft.oopp.group39.building.exceptions.BuildingNotFoundException;
import nl.tudelft.oopp.group39.building.repositories.BuildingRepository;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    /**
     * Doc. TODO Sven
     */
    public List<Building> filterBuildings(
        int capacity,
        String building,
        String location,
        LocalTime open,
        LocalTime closed
    ) {
        int[] buildingIds = buildingRepository.filterBuildingsOnLocationAndNameAndTime(
            building,
            location,
            open,
            closed
        );
        List<Long> resBuildingIds = new ArrayList<>();
        for (int buildingId : buildingIds) {
            if (roomRepository.getRoomsByBuildingId(buildingId).size() > 0) {
                int maxCapacity = roomRepository.getMaxRoomCapacityByBuildingId(buildingId);
                if (capacity <= maxCapacity) {
                    resBuildingIds.add((long) buildingId);
                }
            }
        }
        return (
            resBuildingIds.size() > 0
            ? buildingRepository.getAllBuildingsByIds(resBuildingIds)
            : new ArrayList<>()
        );
    }

    public List<Building> listBuildings() {
        return buildingRepository.findAll();
    }

    public Building readBuilding(long id) throws BuildingNotFoundException {
        return buildingRepository.findById(id)
            .orElseThrow(() -> new BuildingNotFoundException((int) id));
    }

    /**
     * Doc. TODO Sven
     */
    public Building deleteBuilding(long id) throws BuildingNotFoundException {
        try {
            Building rf = readBuilding(id);
            buildingRepository.delete(readBuilding(id));
            return rf;

        } catch (BuildingNotFoundException e) {
            throw new BuildingNotFoundException((int) id);
        }
    }

    /**
     * Doc. TODO Sven
     */
    public Building createBuilding(Building newBuilding) {
        try {
            Building building = readBuilding((int) newBuilding.getId());
            throw new BuildingExistsException((int) building.getId());

        } catch (BuildingNotFoundException e) {
            buildingRepository.save(newBuilding);
            return newBuilding;
        }
    }

    /**
     * Doc. TODO Sven
     */
    public Building updateBuilding(int id, Building newBuilding) throws BuildingNotFoundException {
        return buildingRepository.findById((long) id)
            .map(building -> {
                newBuilding.setId(id);
                building = newBuilding;
                return buildingRepository.save(building);
            }).orElseThrow(() -> new BuildingNotFoundException(id));
    }

}
