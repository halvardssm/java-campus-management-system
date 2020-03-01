package nl.tudelft.oopp.group39.dbSeed;

import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.repositories.BuildingRepository;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.repositories.FacilityRepository;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@RestController
public class DbSeedMap {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private UserRepository userRepository;


    //To seed the database: type in cmd ->
    // curl localhost:8080/db-seed
    @GetMapping("/db-seed")
    @ResponseBody
    public String db_seed() {
        db_seed_facilities();
        db_seed_buildings();
        db_seed_rooms();
        db_seed_users();
        return "saved";
    }

    //To seed the database (for testing purposes)
    public void db_seed_buildings() {
        LocalTime open = LocalTime.now();//.minusHours(3);
        LocalTime closed = LocalTime.now();//.plusHours(3);
        Building b = new Building("test", "test", "test", open, closed);
        buildingRepository.save(b);
        b = new Building("new", "new", "new", open, closed);
//        b = new Building(1,"new","new","new", open.plusHours(4), closed.minusHours(4));
        buildingRepository.save(b);
    }

    public void db_seed_facilities() {
        Set<Room> rooms = new HashSet<Room>();
        LocalTime open = LocalTime.now();//.minusHours(3);
        LocalTime closed = LocalTime.now();//.plusHours(3);
        Facility b = new Facility("smartboard", rooms);
        facilityRepository.save(b);
        b = new Facility("whiteboard", rooms);
        facilityRepository.save(b);
        b = new Facility("projectroom", rooms);
        facilityRepository.save(b);
        b = new Facility("projector", rooms);
        facilityRepository.save(b);
        b = new Facility("computers", rooms);
        facilityRepository.save(b);
    }

    public void db_seed_rooms() {
        Set<Facility> facilities = new HashSet<>();
        long buildingId = buildingRepository.findAll().get(0).getId();
        Room n = new Room(buildingId, 10, true, "test1", facilities);
        roomRepository.save(n);
        Set<Room> rooms = new HashSet<>();
        facilities.add(facilityRepository.getOne((long) 1));
        n = new Room(buildingId, 6, true, "test2", facilities);
        roomRepository.save(n);
        rooms.add(n);
        facilities.add(facilityRepository.getOne((long) 2));
        n = new Room(buildingId + 1, 15, false, "test3", facilities);
        rooms.add(n);
        roomRepository.save(n);
        facilityRepository.getOne((long) 1).setRooms(rooms);
        rooms.clear();
        rooms.add(n);
        facilityRepository.getOne((long) 2).setRooms(rooms);
    }

    public void db_seed_users() {
        User b = new User("1", "t", "t", null, null);
        userRepository.save(b);
        b = new User("2", "t", "t", null, null);
        userRepository.save(b);
    }
}
