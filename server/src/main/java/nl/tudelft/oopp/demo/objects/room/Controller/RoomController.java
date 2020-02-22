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

    @GetMapping("")
    public List<Room> ListRooms() {
        return service.listRooms();
    }

    @DeleteMapping("/{id}")
    public void DeleteRoom(@PathVariable int id) {
        service.deleteRoom(id);
    }

    @PostMapping("")
    @ResponseBody
    public String AddRoom(@RequestBody Room newRoom) {
        int[] facilities = new int[0];
        service.addRoom(newRoom.getCapacity(),newRoom.getOnlyStaff(),facilities,newRoom.getBuilding(),newRoom.getDescription());
        return "saved";
    }

    @GetMapping("/filter")
    @ResponseBody
    public List<Room> FilterRooms(@RequestParam int capacity, @RequestParam boolean onlyStaff, @RequestParam int[] facilities, @RequestParam String building, @RequestParam String location){
        return service.filterRooms(capacity,onlyStaff,facilities, building, location);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Room ReadRoom(@PathVariable int id) {
        return service.readRoom((long) id);
    }

    @PutMapping ("/{id}")
    @ResponseBody
    public Room updateRoom(@RequestBody Room updated, @PathVariable int id, @PathVariable int[] facilities) {
        service.deleteRoomFacilities(id, facilities);
        service.createRoomFacilities(id,facilities);
        return service.updateRoom(updated, id);
    }

    //long id, int capacity, boolean onlyStaff, int[] facilities, long buildingId, String description
}
