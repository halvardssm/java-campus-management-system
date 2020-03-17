//package nl.tudelft.oopp.group39.reservation.services;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;
//import javassist.NotFoundException;
//import nl.tudelft.oopp.group39.reservation.entities.Reservation;
//import nl.tudelft.oopp.group39.reservation.enums.ReservationTypes;
//import nl.tudelft.oopp.group39.reservation.repositories.ReservationRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class ReservationServiceTest {
//    private static final Reservation testReservation = new Reservation(
//        ReservationTypes.RESERVATION,
//        LocalDate.now(ZoneId.of("Europe/Paris")),
//        LocalDate.now(ZoneId.of("Europe/Paris")).plusDays(1),
//        null
//    );
//
//    @Autowired
//    private ReservationService reservationService;
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @BeforeEach
//    void setUp() {
//        Reservation reservation = reservationRepository.save(testReservation);
//        testReservation.setId(reservation.getId());
//    }
//
//    @AfterEach
//    void tearDown() {
//        reservationRepository.deleteAll();
//        testReservation.setId(null);
//    }
//
//    @Test
//    void listReservations() {
//        List<Reservation> reservations = reservationService.listReservations();
//
//        assertEquals(1, reservations.size());
//        assertEquals(testReservation, reservations.get(0));
//    }
//
//    @Test
//    void readReservation() throws NotFoundException {
//        Reservation reservation = reservationService.readReservation(testReservation.getId());
//
//        assertEquals(testReservation, reservation);
//    }
//
//    @Test
//    void createReservation() {
//        Reservation reservation = testReservation;
//        reservation.setType(ReservationTypes.HOLIDAY);
//        Reservation reservation2 = reservationService.createReservation(reservation);
//
//        assertEquals(reservation, reservation2);
//    }
//
//    @Test
//    void updateReservation() throws NotFoundException {
//        Reservation reservation = testReservation;
//        reservation.setType(ReservationTypes.HOLIDAY);
//        Reservation reservation2 = reservationService.updateReservation(testReservation.getId(), reservation);
//
//        assertEquals(reservation, reservation2);
//    }
//
//    @Test
//    void deleteReservation() {
//        reservationService.deleteReservation(testReservation.getId());
//
//        assertEquals(new ArrayList<>(), reservationService.listReservations());
//    }
//}