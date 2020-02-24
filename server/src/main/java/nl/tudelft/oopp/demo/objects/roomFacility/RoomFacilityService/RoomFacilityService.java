package nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityService;

import nl.tudelft.oopp.demo.objects.roomFacility.Entities.RoomFacility;
import nl.tudelft.oopp.demo.objects.roomFacility.Exceptions.RoomFacilityExistsException;
import nl.tudelft.oopp.demo.objects.roomFacility.Exceptions.RoomFacilityNotFoundException;
import nl.tudelft.oopp.demo.objects.roomFacility.Repositories.RoomFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomFacilityService {

    @Autowired
    private RoomFacilityRepository roomFacilityRepository;

    public RoomFacility readRoomFacility(long id) throws RoomFacilityNotFoundException {
        return roomFacilityRepository.findById(id).orElseThrow(() -> new RoomFacilityNotFoundException((int)id));
    }

    public List<RoomFacility> listRoomFacilities() {
        return roomFacilityRepository.findAll();
    }

    public RoomFacility createRoomFacility(RoomFacility newRoomFacility) {
        try {
            RoomFacility roomFacility = readRoomFacility((int)newRoomFacility.getId());
            throw new RoomFacilityExistsException((int)newRoomFacility.getId());
        }
        catch (RoomFacilityNotFoundException e){
            roomFacilityRepository.save(newRoomFacility);
            return newRoomFacility;

        }
    }

    public RoomFacility updateRoomFacility(RoomFacility newRoomFacility, int id) throws RoomFacilityNotFoundException {
        return roomFacilityRepository.findById((long) id)
                .map(room -> {
                    room.setRoomId(newRoomFacility.getRoomId());
                    room.setFacilityId(newRoomFacility.getFacilityId());
                    return roomFacilityRepository.save(room);
                }).orElseThrow(() -> new RoomFacilityNotFoundException(id));
    }

    public RoomFacility deleteRoomFacility(int id) throws RoomFacilityNotFoundException {
        try {
            RoomFacility rf = readRoomFacility((long) id);
            roomFacilityRepository.delete(readRoomFacility((long) id));
            return rf;
        }
        catch (RoomFacilityNotFoundException e) {
            throw new RoomFacilityNotFoundException(id);
        }
    }

    public void createRoomFacilities(long newId,int[] facilities) {
        for (int facility : facilities) {
            long newFId = roomFacilityRepository.findAll().size() > 0 ? roomFacilityRepository.getMaxId() + 1 : 0;
            RoomFacility f = new RoomFacility(newFId, newId, facility);
            createRoomFacility(f);
        }
    }

    public void deleteRoomFacilities(long id,String type) {
        int[] facilityIds = new int[0];
        switch(type) {
            case "room":
                facilityIds = roomFacilityRepository.getRoomFacilityIdsByRoomId(id);
                break;
            default:
                facilityIds = roomFacilityRepository.getRoomFacilityIdsByFacilityId(id);
        }
        for (int facility : facilityIds) {
            deleteRoomFacility(facility);
        }
    }

    public void addRoomFacility(int roomId, int facilityId) {
        int newId = roomFacilityRepository.findAll().size() > 0 ? roomFacilityRepository.getMaxId() + 1 : 0;
        RoomFacility n = new RoomFacility(newId,roomId,facilityId);
        createRoomFacility(n);
    }
}
