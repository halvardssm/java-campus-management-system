package nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityActions;

import nl.tudelft.oopp.demo.objects.roomFacility.*;
import nl.tudelft.oopp.demo.objects.roomFacility.Exceptions.RoomFacilityExistsException;
import nl.tudelft.oopp.demo.objects.roomFacility.Exceptions.RoomFacilityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RoomFacilityActions {

    @Autowired
    private RoomFacilityRepository roomFacilityRepository;

    public RoomFacility readRoomFacility(long id) throws RoomFacilityNotFoundException {
        return roomFacilityRepository.findById(id).orElseThrow(() -> new RoomFacilityNotFoundException((int)id));
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

}
