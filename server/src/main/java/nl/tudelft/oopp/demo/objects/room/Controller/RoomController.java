package nl.tudelft.oopp.demo.objects.room.Controller;

import nl.tudelft.oopp.demo.objects.room.Entities.Room;
import nl.tudelft.oopp.demo.objects.room.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService service;

    @GetMapping("/list")
    public List<Room> ListRooms() {
        return service.listRooms();
    }

    @GetMapping("/delete")
    public void DeleteRoom(@RequestParam int id) {
        service.deleteRoom(id);
    }

    @GetMapping("/add")
    @ResponseBody
    public String AddRoom(@RequestParam int capacity, @RequestParam boolean onlyStaff, @RequestParam int[] facilities,@RequestParam int buildingId, @RequestParam String description) {
        service.addRoom(16,true,facilities,1,"test2");
        return "saved";
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<Room> FilterRooms(@RequestParam int capacity, @RequestParam boolean onlyStaff, @RequestParam int[] facilities, @RequestParam String building, @RequestParam String location){
        return service.filterRooms(capacity,onlyStaff,facilities, building, location);
    }

    @GetMapping("/read")
    @ResponseBody
    public Room ReadRoom(@RequestParam int id) {
        return service.readRoom((long) id);
    }

    @GetMapping ("/update")
    @ResponseBody
    public Room updateRoom(@RequestParam int id,@RequestParam int capacity, @RequestParam boolean onlyStaff, @RequestParam int[] facilities, @RequestParam int buildingId, @RequestParam String description) {
        Room updated = new Room((long)id, capacity, buildingId, onlyStaff, description);
        service.deleteRoomFacilities(id, facilities);
        service.createRoomFacilities(id,facilities);
        return service.updateRoom(updated, id);
    }

    //long id, int capacity, boolean onlyStaff, int[] facilities, long buildingId, String description
}
