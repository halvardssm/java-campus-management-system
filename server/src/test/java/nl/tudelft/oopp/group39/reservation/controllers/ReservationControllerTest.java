//package nl.tudelft.oopp.group39.reservation.controllers;
//
//import static nl.tudelft.oopp.group39.reservation.controllers.ReservationController.REST_MAPPING;
//import static org.hamcrest.Matchers.hasSize;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonSerializer;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.HashSet;
//import nl.tudelft.oopp.group39.auth.services.JwtService;
//import nl.tudelft.oopp.group39.config.Constants;
//import nl.tudelft.oopp.group39.reservation.entities.Reservation;
//import nl.tudelft.oopp.group39.reservation.repositories.ReservationRepository;
//import nl.tudelft.oopp.group39.reservation.services.ReservationService;
//import nl.tudelft.oopp.group39.user.entities.User;
//import nl.tudelft.oopp.group39.user.enums.Role;
//import nl.tudelft.oopp.group39.user.services.UserService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class ReservationControllerTest {
//    private static final User testUser = new User(
//        "test",
//        "test@tudelft.nl",
//        "test",
//        null,
//        Role.ADMIN,
//        new HashSet<>(),
//        new HashSet<>()
//    );
//    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
//        (JsonSerializer<LocalDate>) (date, typeOfT, context)
//            -> new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE))).create();
//    private static Reservation testReservation = new Reservation(
//        LocalDateTime.now(ZoneId.of("Europe/Paris")),
//        null,
//        null
//    );
//    private String jwt;
//
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ReservationRepository reservationRepository;
//    @Autowired
//    private ReservationService reservationService;
//    @Autowired
//    private ReservationController reservationController;
//
//    @BeforeEach
//    void setUp() {
//        reservationRepository.deleteAll();
//        Reservation reservation = reservationService.createReservation(testReservation);
//        testReservation.setId(reservation.getId());
//        userService.createUser(testUser);
//        jwt = jwtService.encrypt(testUser);
//    }
//
//    @AfterEach
//    void tearDown() {
//        reservationRepository.deleteAll();
//        testReservation.setId(null);
//        userService.deleteUser(testUser.getUsername());
//    }
//
//    @Test
//    void listReservations() throws Exception {
//        mockMvc.perform(get(REST_MAPPING))
//            .andExpect(jsonPath("$.body").isArray())
//            .andExpect(jsonPath("$.body", hasSize(1)))
//            .andExpect(jsonPath("$.body[0].type", is(testReservation.getType().name())))
//            .andExpect(jsonPath("$.body[0].startDate", is(testReservation.getStartDate().toString())))
//            .andExpect(jsonPath("$.body[0].endDate", is(testReservation.getEndDate().toString())))
//            .andExpect(jsonPath("$.body[0].rooms").isArray())
//            .andExpect(jsonPath("$.body[0].rooms", hasSize(0)));
//    }
//
//    @Test
//    void createReservation() throws Exception {
//        Reservation reservation = testReservation;
//        reservation.setType(ReservationTypes.HOLIDAY);
//        String json = gson.toJson(reservation);
//
//        mockMvc.perform(post(REST_MAPPING)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(json)
//            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
//            .andExpect(status().isCreated())
//            .andExpect(jsonPath("$.body.type", is(reservation.getType().name())))
//            .andExpect(jsonPath("$.body.startDate", is(reservation.getStartDate().toString())))
//            .andExpect(jsonPath("$.body.endDate", is(reservation.getEndDate().toString())))
//            .andExpect(jsonPath("$.body.rooms").isArray())
//            .andExpect(jsonPath("$.body.rooms", hasSize(0)));
//    }
//
//    @Test
//    void readReservation() throws Exception {
//        mockMvc.perform(get(REST_MAPPING + "/" + testReservation.getId()))
//            .andExpect(jsonPath("$.body").isMap())
//            .andExpect(jsonPath("$.body.type", is(testReservation.getType().name())))
//            .andExpect(jsonPath("$.body.startDate", is(testReservation.getStartDate().toString())))
//            .andExpect(jsonPath("$.body.endDate", is(testReservation.getEndDate().toString())))
//            .andExpect(jsonPath("$.body.rooms").isArray())
//            .andExpect(jsonPath("$.body.rooms", hasSize(0)));
//    }
//
//    @Test
//    void updateReservation() throws Exception {
//        Reservation reservation = testReservation;
//        reservation.setType(ReservationTypes.HOLIDAY);
//        reservation.setStartDate(LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(3));
//        reservation.setEndDate(LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(5));
//        String json = gson.toJson(reservation);
//
//        mockMvc.perform(put(REST_MAPPING + "/" + testReservation.getId())
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(json)
//            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.body").isMap())
//            .andExpect(jsonPath("$.body.type", is(ReservationTypes.HOLIDAY.name())))
//            .andExpect(jsonPath("$.body.startDate", is(reservation.getStartDate().toString())))
//            .andExpect(jsonPath("$.body.endDate", is(reservation.getEndDate().toString())))
//            .andExpect(jsonPath("$.body.rooms").isArray())
//            .andExpect(jsonPath("$.body.rooms", hasSize(0)));
//    }
//
//    @Test
//    void deleteReservation() throws Exception {
//        mockMvc.perform(delete(REST_MAPPING + "/" + testReservation.getId())
//            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.body").doesNotExist());
//    }
//
//    @Test
//    void testError() {
//        assertEquals("Target object must not be null; nested exception is "
//                + "java.lang.IllegalArgumentException: Target object must not be null",
//            reservationController.createReservation(null).getBody().getError());
//
//        assertEquals("Reservation 0 not found", reservationController.readReservation(0).getBody().getError());
//
//        assertEquals("Reservation 0 not found", reservationController.updateReservation(0, null).getBody().getError());
//    }
//}