package nl.tudelft.oopp.demo.objects.room.RoomActions;

import nl.tudelft.oopp.demo.objects.room.Exceptions.*;
import nl.tudelft.oopp.demo.objects.room.Room;
import nl.tudelft.oopp.demo.objects.room.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityActions.RoomFacilityActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RoomActions extends RoomFacilityActions {

    @Autowired
    private RoomRepository roomRepository;

    public Room readRoom(long id) throws RoomNotFoundException {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException((int)id));
    }

    public Room createRoom(Room newRoom) {
        try {
            Room room = readRoom((int)newRoom.getId());
            throw new RoomExistsException((int)newRoom.getId());
        }
        catch (RoomNotFoundException e){
            roomRepository.save(newRoom);
            return newRoom;

        }
    }

    public Room updateRoom(Room newRoom, int id) throws RoomNotFoundException {
        return roomRepository.findById((long) id)
                .map(room -> {
                    room.setBuilding(newRoom.getBuilding());
                    room.setCapacity(newRoom.getCapacity());
                    room.setDescription(newRoom.getDescription());
                    room.setOnlyStaff(newRoom.getOnlyStaff());
                    return roomRepository.save(room);
                }).orElseThrow(() -> new RoomNotFoundException(id));
    }

}
