package nl.tudelft.oopp.group39.DbSeed;

import nl.tudelft.oopp.group39.building.Entities.Building;
import nl.tudelft.oopp.group39.building.Repositories.BuildingRepository;
import nl.tudelft.oopp.group39.facility.Entities.Facility;
import nl.tudelft.oopp.group39.facility.Repositories.FacilityRepository;
import nl.tudelft.oopp.group39.room.Entities.Room;
import nl.tudelft.oopp.group39.room.Repositories.RoomRepository;
import nl.tudelft.oopp.group39.roomFacility.Entities.RoomFacility;
import nl.tudelft.oopp.group39.roomFacility.Repositories.RoomFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
public class DbSeedMap {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomFacilityRepository roomFacilityRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private FacilityRepository facilityRepository;


    //To seed the database: type in cmd ->
    // curl localhost:8080/db-seed
    @GetMapping("/db-seed")
    @ResponseBody
    public String db_seed() {
        db_seed_rooms();
        db_seed_room_facilities();
        db_seed_buildings();
        db_seed_facilities();
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
    public void db_seed_facilities() {
        LocalTime open = LocalTime.now();//.minusHours(3);
        LocalTime closed = LocalTime.now();//.plusHours(3);
        Facility b = new Facility(0,"smartboard");
        facilityRepository.save(b);
        b = new Facility(1,"whiteboard");
        facilityRepository.save(b);
        b = new Facility(2,"projectroom");
        facilityRepository.save(b);
        b = new Facility(3,"projector");
        facilityRepository.save(b);
        b = new Facility(4,"computers");
        facilityRepository.save(b);
    }
}
