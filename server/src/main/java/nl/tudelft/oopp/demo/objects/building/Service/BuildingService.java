package nl.tudelft.oopp.demo.objects.building.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.objects.building.Entities.Building;
import nl.tudelft.oopp.demo.objects.building.Exceptions.BuildingExistsException;
import nl.tudelft.oopp.demo.objects.building.Exceptions.BuildingNotFoundException;
import nl.tudelft.oopp.demo.objects.building.Repositories.BuildingRepository;
import nl.tudelft.oopp.demo.objects.room.Repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    public List<Building> filterBuildings(int capacity, String building, String location) {
        LocalTime open = LocalTime.now().plusHours(2);
        LocalTime closed = LocalTime.now().minusHours(2);
        int[] buildingIds = buildingRepository.filterBuildingsOnLocationAndNameAndTime(building, location, open, closed);

        List<Long> resBuildingIds = new ArrayList<>();
        for (int buildingId : buildingIds) {
            if (roomRepository.getRoomsByBuildingId(buildingId).size() > 0) {
                int maxCapacity = roomRepository.getMaxRoomCapacityByBuildingId(buildingId);
                if (capacity <= maxCapacity) {
                    resBuildingIds.add((long)buildingId);
                }
            }
        }
        return (resBuildingIds.size() > 0 ? buildingRepository.getAllBuildingsByIds(resBuildingIds) : new ArrayList<Building>());
    }

    public void addBuilding(String name, String location, String description){//, LocalTime open, LocalTime closed) {
        LocalTime open = LocalTime.now().minusHours(4);
        LocalTime closed = LocalTime.now().plusHours(4);
        int newId = buildingRepository.findAll().size() > 0 ? buildingRepository.getMaxId() + 1 : 0;
        Building n = new Building(newId,name,location,description, open, closed);
        createBuilding(n);
    }

    public List<Building> listBuildings(){
        return buildingRepository.findAll();
    }

    public Building readBuilding(long id) throws BuildingNotFoundException {
        return buildingRepository.findById(id).orElseThrow(() -> new BuildingNotFoundException((int) id));
    }

    public Building deleteBuilding(long id) throws BuildingNotFoundException {
        try {
            Building rf = readBuilding(id);
            buildingRepository.delete(readBuilding(id));
            return rf;
        }
        catch (BuildingNotFoundException e) {
            throw new BuildingNotFoundException((int)id);
        }
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
