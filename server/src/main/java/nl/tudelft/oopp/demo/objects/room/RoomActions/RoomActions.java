package nl.tudelft.oopp.demo.objects.room.RoomActions;

import nl.tudelft.oopp.demo.objects.room.Exceptions.*;
import nl.tudelft.oopp.demo.objects.room.Room;
import nl.tudelft.oopp.demo.objects.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RoomActions {

    @Autowired
    private RoomRepository roomRepository;

    public Room readRoom(int id) throws RoomNotFoundException {
        List<Room> rooms = roomRepository.findById(id);
        if(rooms.size() > 0) {
            return rooms.get(0);
        }
        throw new RoomNotFoundException(id);
    }

    public Room createRoom(Room newRoom) {
        try {
            Room room = readRoom(newRoom.getId());
            throw new RoomExistsException(newRoom.getId());
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
