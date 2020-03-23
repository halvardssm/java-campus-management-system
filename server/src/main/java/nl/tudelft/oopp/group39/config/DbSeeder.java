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
            Role.ADMIN,
            null,
            null
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
        LocalTime open = LocalTime.of(9, 0);//.minusHours(3);
        LocalTime closed = LocalTime.of(20, 0);//.plusHours(3);
        Building b = new Building("test", "test", "test", open, closed, null);
        buildingService.createBuilding(b);
        b = new Building("new", "new", "new", open, closed, null);
        buildingService.createBuilding(b);

        System.out.println("[SEED] Buildings created");
    }

    private void initRooms() {
        Set<Facility> facilities = new HashSet<>();
        Set<Booking> bookings = new HashSet<>();

        roomService.createRoom(new Room(1, "test", 10, true, "test1", facilities, bookings));

        facilities.add(facilityService.readFacility(1));
        roomService.createRoom(new Room(1, "lala", 6, true, "test2", facilities, bookings));

        facilities.add(facilityService.readFacility(2));
        roomService.createRoom(
            new Room(2, "another one", 15, false, "test3", facilities, bookings));

        System.out.println("[SEED] Rooms created");
    }

    private void initEvents() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        Room room = new Room(1, "lala", 0, false, null, new HashSet<>(), new HashSet<>());
        HashSet<Room> rooms = new HashSet<>(List.of(room));
        eventService.createEvent(new Event(EventTypes.EVENT, today, tomorrow, rooms));

        System.out.println("[SEED] Events created");
    }

    private void initBookings() {
        LocalDate date = LocalDate.now();
        LocalTime start = LocalTime.of(13, 0);
        LocalTime end = LocalTime.of(15, 0);
        User user = userService.readUser("admin");

        List<Room> rooms = roomService.listRooms();

        Booking b1 = new Booking(date, start, end, user, rooms.get(0));
        bookingService.createBooking(b1);
        Booking b2 = new Booking(date, start, end, user, rooms.get(1));
        bookingService.createBooking(b2);

        Booking b3 = new Booking(date.plusDays(1), start, end, user, rooms.get(0));
        bookingService.createBooking(b3);
        Booking b4 = new Booking(date.plusDays(1), start, end, user, rooms.get(1));
        bookingService.createBooking(b4);

        System.out.println("[SEED] Bookings created");
    }

    private void initBikes() {
        Building building = buildingService.listBuildings().get(0);

        Bike bike1 = new Bike(BikeType.CITY, 5.6, building, null);
        Bike bike2 = new Bike(BikeType.CITY, 6.7, building, null);
        Bike bike3 = new Bike(BikeType.CITY, 7.8, building, null);

        bikeService.createBike(bike1);
        bikeService.createBike(bike2);
        bikeService.createBike(bike3);

        System.out.println("[SEED] Bikes created");
    }

    private void initFoods() {
        Building building = buildingService.listBuildings().get(0);

        Food food1 = new Food("Stew", "A warm pot of deliciousness", 5.6, building, null);
        Food food2 = new Food("Meatballs", "Balls of meat", 6.7, building, null);
        Food food3 = new Food("Carrot Cake", "I mean cake, it's simply good", 7.8, building, null);

        foodService.createFood(food1);
        foodService.createFood(food2);
        foodService.createFood(food3);

        System.out.println("[SEED] Foods created");
    }

    private void initReservations() {

        Reservation reservation = reservationService.createReservation(new Reservation(
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(2),
            roomService.listRooms().get(0),
            userService.readUser("admin"),
            null
        ));

        ReservationAmount reservationAmount1 = new ReservationAmount(
            5,
            reservation,
            foodService.listFoods(new HashMap<>()).get(0)
        );
        ReservationAmount reservationAmount2 = new ReservationAmount(
            1,
            reservation,
            bikeService.listBikes(new HashMap<>()).get(0)
        );

        reservationAmountService.createReservation(reservationAmount1);
        reservationAmountService.createReservation(reservationAmount2);

        System.out.println("[SEED] Reservations created");
    }
}
