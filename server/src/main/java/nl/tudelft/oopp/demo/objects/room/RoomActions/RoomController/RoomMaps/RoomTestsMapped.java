package nl.tudelft.oopp.demo.objects.room.RoomActions.RoomController.RoomMaps;

import nl.tudelft.oopp.demo.objects.building.Building;
import nl.tudelft.oopp.demo.objects.building.BuildingRepository;
import nl.tudelft.oopp.demo.objects.room.Room;
import nl.tudelft.oopp.demo.objects.room.RoomActions.RoomController.RoomController;
import nl.tudelft.oopp.demo.objects.room.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacility;
import nl.tudelft.oopp.demo.objects.roomFacility.RoomFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class RoomTestsMapped extends RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @GetMapping("fb")
    @ResponseBody
    public int[] fb(@RequestParam String name) {
        return buildingRepository.filterBuildingsOnName(name);
    }
    @GetMapping("fb2")
    @ResponseBody
    public int[] fb2() {
        return buildingRepository.filterBuildingsOnName("test");
    }
    @GetMapping("roomId")
    @ResponseBody
    public Room getRbyID(@RequestParam int id) {
        return roomRepository.getRoomById(id);
    }

    @GetMapping("roomFilterAll")
    @ResponseBody
    public List<Room> RoomFilterAll() {
        int[] facilities = {};
        return filterRooms(0,true,facilities,"","");
    }
    @GetMapping("roomFilterTest")
    @ResponseBody
    public List<Room> RoomFilterTest() {
        int[] facilities = {};
        return filterRooms(0,true,facilities,"test","test");
    }
    @GetMapping("roomFilter")
    @ResponseBody
    public List<Room> RoomFilter() {
        int[] facilities = {0,1,2};
        return filterRooms(0,true,facilities,"test","test");
    }

    //To test this in cmd (first seed the database!) ->
    // curl localhost:8080/room, curl localhost:8080/building or curl localhost:8080/roomFacility
    @GetMapping("room")
    @ResponseBody
    //Returns a list of all rooms
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }
    //Returns a list of all buildings
    @GetMapping("building")
    @ResponseBody
    public List<Building> getBuildings() {
        List<Building> buildings = buildingRepository.findAll();
        return buildings;
    }
    //Returns a list of all room-facilities (relation between rooms having facilities)
    @GetMapping("roomFacility")
    @ResponseBody
    public List<RoomFacility> getRoomFacilities() {
        List<RoomFacility> room_facilities = roomFacilityRepository.findAll();
        return room_facilities;
    }

    //To seed the database: type in cmd ->
    // curl localhost:8080/db-seed
    @GetMapping("db-seed")
    @ResponseBody
    public String db_seed() {
        db_seed_rooms();
        db_seed_room_facilities();
        db_seed_buildings();
        return "saved";
    }
    //To seed the database (for testing purposes)
    public void db_seed_rooms() {
        Room n = new Room(0,0,10,true,"test1");
        roomRepository.save(n);
        n = new Room(1,0,6,true,"test2");
        roomRepository.save(n);
        n = new Room(2,1,15,false,"test3");
        roomRepository.save(n);
    }
    public void db_seed_room_facilities() {
        RoomFacility f = new RoomFacility(0,0,0);
        roomFacilityRepository.save(f);
        f = new RoomFacility(1,0,1);
        roomFacilityRepository.save(f);
        f = new RoomFacility(2,1,2);
        roomFacilityRepository.save(f);
        f = new RoomFacility(3,2,4);
        roomFacilityRepository.save(f);
    }
    public void db_seed_buildings() {
        Building b = new Building(0,"test","test","test");
        buildingRepository.save(b);
        b = new Building(1,"new","new","new");
        buildingRepository.save(b);
    }
}
