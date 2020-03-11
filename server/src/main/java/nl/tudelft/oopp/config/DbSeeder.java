package nl.tudelft.oopp.config;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.building.entities.Building;
import nl.tudelft.oopp.building.services.BuildingService;
import nl.tudelft.oopp.facility.entities.Facility;
import nl.tudelft.oopp.facility.services.FacilityService;
import nl.tudelft.oopp.room.entities.Room;
import nl.tudelft.oopp.room.services.RoomService;
import nl.tudelft.oopp.user.entities.User;
import nl.tudelft.oopp.user.enums.Role;
import nl.tudelft.oopp.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Seeds the database on application load.
 */
@Component
public class DbSeeder {
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private FacilityService facilityService;

    /**
     * Initiates the db with all the roles.
     */
    public void seedDatabase() {
        System.out.println("[SEED] Seeding started");
        initUsers();
        initFacilities();
        initBuildings();
        initRooms();
        System.out.println("[SEED] Seeding completed");
    }

    /**
     * Initiates the database with an admin user with all authorities.
     */
    private void initUsers() {
        User user = new User(
            "admin",
            "admin@tudelft.nl",
            "pwd",
            null,
            Role.ADMIN
        );

        userService.createUser(user);
        System.out.println("[SEED] Admin user created");
    }

    private void initFacilities() {
        Set<Room> rooms = new HashSet<>();
        facilityService.createFacility(new Facility("smartboard", rooms));
        facilityService.createFacility(new Facility("whiteboard", rooms));
        facilityService.createFacility(new Facility("projectroom", rooms));
        facilityService.createFacility(new Facility("projector", rooms));
        facilityService.createFacility(new Facility("computers", rooms));

        System.out.println("[SEED] Facilities created");
    }

    private void initBuildings() {
        LocalTime open = LocalTime.now();//.minusHours(3);
        LocalTime closed = LocalTime.now();//.plusHours(3);
        Building b = new Building("test", "test", "test", open, closed);
        buildingService.createBuilding(b);
        b = new Building("new", "new", "new", open, closed);
        buildingService.createBuilding(b);

        System.out.println("[SEED] Buildings created");
    }

    private void initRooms() {
        Set<Facility> facilities = new HashSet<>();

        roomService.createRoom(new Room(1, 10, true, "test1", facilities));

        facilities.add(facilityService.readFacility(1));

        roomService.createRoom(new Room(1, 6, true, "test2", facilities));

        facilities.add(facilityService.readFacility(2));

        roomService.createRoom(new Room(2, 15, false, "test3", facilities));

        System.out.println("[SEED] Rooms created");
    }
}
