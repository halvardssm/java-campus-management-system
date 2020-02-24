package nl.tudelft.oopp.demo.objects.DbSeed;

import nl.tudelft.oopp.demo.objects.building.Entities.Building;
import nl.tudelft.oopp.demo.objects.building.Repositories.BuildingRepository;
import nl.tudelft.oopp.demo.objects.room.Entities.Room;
import nl.tudelft.oopp.demo.objects.room.Repositories.RoomRepository;
import nl.tudelft.oopp.demo.objects.roomFacility.Entities.RoomFacility;
import nl.tudelft.oopp.demo.objects.roomFacility.Repositories.RoomFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalTime;

@Controller
public class DbSeedMap {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;


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
        LocalTime open = LocalTime.now();//.minusHours(3);
        LocalTime closed = LocalTime.now();//.plusHours(3);
        Building b = new Building(0,"test","test","test", open, closed);
        buildingRepository.save(b);
        b = new Building(1,"new","new","new", open, closed);
//        b = new Building(1,"new","new","new", open.plusHours(4), closed.minusHours(4));
        buildingRepository.save(b);
    }
}
