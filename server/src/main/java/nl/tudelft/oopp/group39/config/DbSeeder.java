package nl.tudelft.oopp.group39.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.services.BuildingService;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.event.services.EventService;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.facility.services.FacilityService;
import nl.tudelft.oopp.group39.reservable.entities.Bike;
import nl.tudelft.oopp.group39.reservable.entities.Food;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;
import nl.tudelft.oopp.group39.reservable.services.BikeService;
import nl.tudelft.oopp.group39.reservable.services.FoodService;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.reservation.services.ReservationAmountService;
import nl.tudelft.oopp.group39.reservation.services.ReservationService;
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
    private BookingService bookingService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BikeService bikeService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationAmountService reservationAmountService;

    /**
     * Initiates the db with all the roles.
     */
    public void seedDatabase() {
        System.out.println("[SEED] Seeding started");
        initUsers();
        initFacilities();
        initBuildings();
        initRooms();
        initBookings();
        initEvents();
        initBikes();
        initFoods();
        initReservations();
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

        userService.createUser(user, true);

        User user2 = new User(
            "student",
            "student@student.tudelft.nl",
            "student123",
            null,
            Role.STUDENT
        );
        userService.createUser(user2, true);
        System.out.println("[SEED] Admin user created");
    }

    /**
     * Initiates the database with the facilities.
     */
    private void initFacilities() {
        Set<Room> rooms = new HashSet<>();
        facilityService.createFacility(new Facility("smartboard", rooms));
        facilityService.createFacility(new Facility("whiteboard", rooms));
        facilityService.createFacility(new Facility("projectroom", rooms));
        facilityService.createFacility(new Facility("projector", rooms));
        facilityService.createFacility(new Facility("computers", rooms));

        System.out.println("[SEED] Facilities created");
    }

    /**
     * Initiates the database with the buildings.
     */
    private void initBuildings() {
        LocalTime open = LocalTime.of(9, 0);//.minusHours(3);
        LocalTime closed = LocalTime.of(20, 0);//.plusHours(3);
        Building b = new Building(
                null,
                "Library",
                "Prometheuseplein 1",
                "A place where TU Delft students can study and lend books",
                LocalTime.of(8, 0),
                LocalTime.of(0, 0),
                null,
                null);
        buildingService.createBuilding(b);

        Building b1 = new Building(
                null,
                "Pulse",
                "Landbergstraat 19",
                "A new education building for TU Delft students to study and take lectures",
                LocalTime.of(8, 0),
                LocalTime.of(0, 0),
                null,
                null);
        buildingService.createBuilding(b1);

        Building b2 = new Building(
            null,
            "EEMCS",
            "Mekelweg 4",
            "Faculty of Electrical Engineering, Maths and Computer Science",
            LocalTime.of(7, 0),
            LocalTime.of(18, 0),
            null,
            null
        );
        buildingService.createBuilding(b2);

        Building b3 = new Building(
            null,
            "Drebbelweg",
            "Drebbelweg 5",
            "Drebbelweg",
            LocalTime.of(6, 0),
            LocalTime.of(17, 30),
            null,
            null
        );
        buildingService.createBuilding(b3);

        System.out.println("[SEED] Buildings created");
    }

    /**
     * Initiates the database with the rooms.
     */
    private void initRooms() {
        final Building b1 = buildingService.readBuilding(1L);
        final Building b2 = buildingService.readBuilding(2L);
        final Building b3 = buildingService.readBuilding(3L);
        final Building b4 = buildingService.readBuilding(4L);
        roomService.createRoom(new Room(
                null,
                 b1,
                "Congress",
                10,
                true,
                "A congress place for staff to meet",
                null,
                null,
                null));

        roomService.createRoom(new Room(
                null,
                 b1,
                "Study room 1",
                10,
                false,
                "A study place for students to\nwork in a group",
                null,
                null,
                null));

        Set<Facility> facilities = new HashSet<>();
        facilities.add(facilityService.readFacility(1L));
        roomService.createRoom(new Room(
            null,
             b1,
            "Study room 2",
            10,
            false,
            "A study place for students to\nwork in a group",
            null,
             facilities,
            null
        ));

        facilities.add(facilityService.readFacility(2L));
        roomService.createRoom(
            new Room(
                    null,
                     b2,
                    "Pulse-Hall 3",
                    60,
                    true,
                    "A lecture room for lecturers to give lectures",
                    null,
                     facilities,
                    null));

        roomService.createRoom(new Room(
            null,
            b3,
            "Lecture Hall Ampere",
            329,
            true,
            "Lecture hall in EEMCS",
            null,
            facilities,
            null
        ));

        roomService.createRoom(new Room(
                null,
                b3,
                "Lecture Hall Boole",
                197,
                true,
                "Lecture hall in EEMCS",
                null,
                facilities,
                null
        ));

        Set<Facility> facilities2 = new HashSet<>();
        facilities2.add(facilityService.readFacility(2L));
        facilities2.add(facilityService.readFacility(3L));

        roomService.createRoom(new Room(
                null,
                b4,
                "Projectruimte 1",
                8,
                false,
                "Project Room 1",
                null,
                facilities2,
                null
        ));

        roomService.createRoom(new Room(
                null,
                b4,
                "Projectruimte 2",
                8,
                false,
                "Project Room 2",
                null,
                facilities2,
                null
        ));

        roomService.createRoom(new Room(
                null,
                b4,
                "Projectruimte 3",
                8,
                false,
                "Project Room 3",
                null,
                facilities2,
                null
        ));

        roomService.createRoom(new Room(
                null,
                b4,
                "Projectruimte 4",
                8,
                false,
                "Project Room 4",
                null,
                facilities2,
                null
        ));

        roomService.createRoom(new Room(
                null,
                b4,
                "Projectruimte 5",
                8,
                false,
                "Project Room 5",
                null,
                facilities2,
                null
        ));

        roomService.createRoom(new Room(
                null,
                b4,
                "Projectruimte 6",
                8,
                false,
                "Project Room 6",
                null,
                facilities2,
                null
        ));

        roomService.createRoom(new Room(
                null,
                b4,
                "Projectruimte 7",
                8,
                false,
                "Project Room 7",
                null,
                facilities2,
                null
        ));

        roomService.createRoom(new Room(
            null,
             b4,
            "Projectruimte 8",
            8,
            false,
            "Project Room 8",
            null,
             facilities2,
            null
        ));

        System.out.println("[SEED] Rooms created");
    }

    /**
     * Initiates the database with events.
     */
    private void initEvents() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime tomorrow = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
        Building b2 = buildingService.readBuilding(2L);
        Room room = new Room(
            null,
             b2,
            "Pulse-Hall 9",
            60,
            true,
            "A lecture room for lecturers to give lectures",
            null,
            new HashSet<>(),
            new HashSet<>()
        );
        HashSet<Room> rooms = new HashSet<>(List.of(room));
        eventService.createEvent(new Event(
            null, "Special day",
            today,
            tomorrow,
            true,
            userService.readUser("admin"),
            rooms
        ));

        System.out.println("[SEED] Events created");
    }

    /**
     * Initiates the database with bookings.
     */
    private void initBookings() {
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.of(13, 0);
        LocalTime end = LocalTime.of(15, 0);
        User user = userService.readUser("admin");

        List<Room> rooms = roomService.listRooms();

        Booking b1 = new Booking(null, date, start, end, user, rooms.get(0));
        bookingService.createBooking(b1);
        Booking b2 = new Booking(null, date, start, end, user, rooms.get(1));
        bookingService.createBooking(b2);

        Booking b3 = new Booking(null, date.plusDays(1), start, end, user, rooms.get(0));
        bookingService.createBooking(b3);
        Booking b4 = new Booking(null, date.plusDays(1), start, end, user, rooms.get(1));
        bookingService.createBooking(b4);

        System.out.println("[SEED] Bookings created");
    }

    /**
     * Initiates the database with bikes.
     */
    private void initBikes() {
        Building building = buildingService.listBuildings(new HashMap<>()).get(0);
        Building building1 = buildingService.listBuildings(new HashMap<>()).get(1);
        Building building2 = buildingService.listBuildings(new HashMap<>()).get(2);
        Building building3 = buildingService.listBuildings(new HashMap<>()).get(3);

        Bike bike1 = new Bike(null, BikeType.CITY, 5.6, building, null);
        Bike bike2 = new Bike(null, BikeType.CITY, 5.6, building, null);
        Bike bike3 = new Bike(null, BikeType.CITY, 5.6, building, null);
        Bike bike4 = new Bike(null, BikeType.ELECTRIC, 7.8, building1, null);
        Bike bike5 = new Bike(null, BikeType.CITY, 5.6, building1, null);
        Bike bike6 = new Bike(null, BikeType.ELECTRIC, 7.8, building1, null);
        Bike bike7 = new Bike(null, BikeType.ELECTRIC, 7.8, building2, null);
        Bike bike8 = new Bike(null, BikeType.ELECTRIC, 7.8, building2, null);
        Bike bike9 = new Bike(null, BikeType.ELECTRIC, 7.8, building2, null);
        Bike bike10 = new Bike(null, BikeType.CITY, 5.6, building3, null);
        Bike bike11 = new Bike(null, BikeType.CITY, 5.6, building3, null);
        Bike bike12 = new Bike(null, BikeType.ELECTRIC, 7.8, building3, null);

        bikeService.createBike(bike1);
        bikeService.createBike(bike2);
        bikeService.createBike(bike3);
        bikeService.createBike(bike4);
        bikeService.createBike(bike5);
        bikeService.createBike(bike6);
        bikeService.createBike(bike7);
        bikeService.createBike(bike8);
        bikeService.createBike(bike9);
        bikeService.createBike(bike10);
        bikeService.createBike(bike11);
        bikeService.createBike(bike12);

        System.out.println("[SEED] Bikes created");
    }

    /**
     * Initiates the database with food.
     */
    private void initFoods() {
        Building building = buildingService.listBuildings(new HashMap<>()).get(0);
        Building building1 = buildingService.listBuildings(new HashMap<>()).get(1);
        Building building2 = buildingService.listBuildings(new HashMap<>()).get(2);
        Building building3 = buildingService.listBuildings(new HashMap<>()).get(3);

        Food food1 = new Food(null, "Stew", "A warm pot of deliciousness", 5.6, building, null);
        Food food2 = new Food(null, "Meatballs", "Balls of meat", 6.7, building, null);
        Food food3 = new Food(
            null,
            "Carrot Cake",
            "I mean cake, it's simply good",
            7.8,
             building,
            null
        );

        Food food4 = new Food(
                null,
                "Oliebollen",
                "A typical Dutch snack",
                2.5,
                 building1,
                null
        );

        Food food5 = new Food(
                null,
                "Stamppot",
                "Mashed potatoes with\nvegetables",
                4.5,
                 building1,
                null
        );

        Food food6 = new Food(
                null,
                "Spaghetti",
                "Pasta with meatballs",
                5.0,
                 building1,
                null
        );

        Food food7 = new Food(
                null,
                "Cookie",
                "A chocolate chip cookie",
                1.0,
                 building2,
                null
        );

        Food food8 = new Food(
                null,
                "Apple Juice",
                "0% sugar",
                1.5,
                 building2,
                null
        );

        Food food9 = new Food(
                null,
                "Bitterballen",
                "12 bitterballen",
                3.5,
                 building2,
                null
        );

        Food food10 = new Food(
                null,
                "Salad",
                "Ham-cheese salad",
                3.5,
                 building3,
                null
        );

        Food food11 = new Food(
                null,
                "Poffertjes",
                "Small little pancakes",
                4.0,
                 building3,
                null
        );

        Food food12 = new Food(
                null,
                "Noodles",
                "Fresh noodles",
                4.0,
                 building3,
                null
        );


        foodService.createFood(food1);
        foodService.createFood(food2);
        foodService.createFood(food3);
        foodService.createFood(food4);
        foodService.createFood(food5);
        foodService.createFood(food6);
        foodService.createFood(food7);
        foodService.createFood(food8);
        foodService.createFood(food9);
        foodService.createFood(food10);
        foodService.createFood(food11);
        foodService.createFood(food12);

        System.out.println("[SEED] Foods created");
    }

    /**
     * Initiates the database with reservations.
     */
    private void initReservations() {

        Reservation reservation = reservationService.createReservation(new Reservation(
            null,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(2),
            roomService.listRooms().get(0),
            userService.readUser("admin"),
            null
        ));

        ReservationAmount reservationAmount1 = new ReservationAmount(
            null,
            5,
            reservation,
            foodService.listFoods(new HashMap<>()).get(0)
        );
        ReservationAmount reservationAmount2 = new ReservationAmount(
            null,
            1,
            reservation,
            bikeService.listBikes(new HashMap<>()).get(0)
        );

        reservationAmountService.createReservation(reservationAmount1);
        reservationAmountService.createReservation(reservationAmount2);

        System.out.println("[SEED] Reservations created");
    }
}
