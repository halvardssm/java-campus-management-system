package nl.tudelft.oopp.group39.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.services.BuildingService;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
import nl.tudelft.oopp.group39.event.services.EventService;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.services.FacilityService;
import nl.tudelft.oopp.group39.reservable.entities.Bike;
import nl.tudelft.oopp.group39.reservable.entities.Food;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;
import nl.tudelft.oopp.group39.reservable.services.BikeService;
import nl.tudelft.oopp.group39.reservable.services.FoodService;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
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
    @Autowired
    private EventService eventService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BikeService bikeService;
    @Autowired
    private FoodService foodService;

    /**
     * Initiates the db with all the roles.
     */
    public void seedDatabase() {
        System.out.println("[SEED] Seeding started");
        initUsers();
        initFacilities();
        initBuildings();
        initRooms();
        initEvents();
//        initBookings();
        initBikes();
        initFoods();
        System.out.println("[SEED] Seeding completed");
    }

    /**
     * Initiates the database with an admin user with all authorities.
     */
    private void initUsers() {
        Set<Booking> bookings = new HashSet<>();
        Set<Reservation> reservations = new HashSet<>();
        User user = new User(
            "admin",
            "admin@tudelft.nl",
            "pwd",
            null,
            Role.ADMIN,
            bookings,
            reservations
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
        Set<Booking> bookings = new HashSet<>();
        roomService.createRoom(new Room(1, 10, true, "test1", facilities, bookings));
        facilities.add(facilityService.readFacility(1));
        roomService.createRoom(new Room(1, 6, true, "test2", facilities, bookings));
        facilities.add(facilityService.readFacility(2));
        roomService.createRoom(new Room(2, 15, false, "test3", facilities, bookings));

        System.out.println("[SEED] Rooms created");
    }

    private void initEvents() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        Room room = new Room(1, 0, false, null, new HashSet<>(), new HashSet<>());
        HashSet<Room> rooms = new HashSet<>(List.of(room));
        eventService.createEvent(new Event(EventTypes.EVENT, today, tomorrow, rooms));

        System.out.println("[SEED] Events created");
    }

    private void initBookings() {
        Set<Facility> facilities = new HashSet<>();
        Set<Booking> bookings = new HashSet<>();
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.now();
        LocalTime end = LocalTime.now();
        User user = userService.readUser("admin");

        Room room = new Room(1, 10, true, "test1", facilities, bookings);

        Booking b = new Booking(date, start, end, user, room);
        bookingService.createBooking(b);

        b = new Booking(date, start, end, user, room);
        bookingService.createBooking(b);

        System.out.println("[SEED] Bookings created");
    }

    private void initBikes() {
        Building building = buildingService.listBuildings().get(0);

        Bike bike1 = new Bike(BikeType.CITY, null, building, 5.6, null);
        Bike bike2 = new Bike(BikeType.CITY, null, building, 6.7, null);
        Bike bike3 = new Bike(BikeType.CITY, null, building, 7.8, null);

        bikeService.createBike(bike1);
        bikeService.createBike(bike2);
        bikeService.createBike(bike3);

        System.out.println("[SEED] Bikes created");
    }

    private void initFoods() {
        Building building = buildingService.listBuildings().get(0);

        Food food1 = new Food("Stew", "A warm pot of deliciousness", building, 5.6, null);
        Food food2 = new Food("Meatballs", "Balls of meat", building, 6.7, null);
        Food food3 = new Food("Carrot Cake", "I mean cake, it's simply good", building, 7.8, null);

        foodService.createFood(food1);
        foodService.createFood(food2);
        foodService.createFood(food3);

        System.out.println("[SEED] Foods created");
    }

    private void initReservations() {


        System.out.println("[SEED] Reservations created");
    }
}
