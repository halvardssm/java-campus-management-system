package nl.tudelft.oopp.group39.reservation.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.reservation.dto.ReservationDto;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationServiceTest extends AbstractTest {
    private final Reservation testReservation = new Reservation(
        null,
        LocalDateTime.now(ZoneId.of("Europe/Paris")),
        LocalDateTime.now(ZoneId.of("Europe/Paris")).plusHours(2),
        null,
        null,
        null
    );
    private final ReservationDto testReservationDto = new ReservationDto(
        null,
        testReservation.getTimeOfPickup().plusDays(2),
        testReservation.getTimeOfDelivery().plusDays(5),
        null,
        null,
        null
    );

    @BeforeEach
    void setUp() {
        Reservation reservation = reservationService.createReservation(testReservation);
        testReservation.setId(reservation.getId());
    }

    @AfterEach
    void tearDown() {
        reservationService.deleteReservation(testReservation.getId());
        testReservation.setId(null);
    }

    @Test
    void listReservations() {
        List<Reservation> reservations = reservationService.listReservations();

        assertEquals(1, reservations.size());
        assertEquals(testReservation.getId(), reservations.get(0).getId());
        assertEquals(
            testReservation.getTimeOfPickup()
                .format(DateTimeFormatter.ISO_DATE),
            reservations.get(0).getTimeOfPickup()
                .format(DateTimeFormatter.ISO_DATE)
        );

        assertEquals(
            testReservation.getTimeOfDelivery()
                .format(DateTimeFormatter.ISO_DATE),
            reservations.get(0).getTimeOfDelivery()
                .format(DateTimeFormatter.ISO_DATE)
        );
    }

    @Test
    void readReservation() throws NotFoundException {
        Reservation reservation = reservationService.readReservation(testReservation.getId());

        assertEquals(testReservation.getId(), reservation.getId());
        assertEquals(testReservation.getTimeOfPickup(), reservation.getTimeOfPickup());
        assertEquals(testReservation.getTimeOfDelivery(), reservation.getTimeOfDelivery());
    }

    @Test
    void deleteAndCreateReservation() {
        reservationService.deleteReservation(testReservation.getId());

        assertEquals(new ArrayList<>(), reservationService.listReservations());

        Reservation reservation = reservationService.createReservation(testReservation);
        testReservation.setId(reservation.getId());

        assertEquals(testReservation.getId(), reservation.getId());
        assertEquals(testReservation.getTimeOfPickup(), reservation.getTimeOfPickup());
        assertEquals(testReservation.getTimeOfDelivery(), reservation.getTimeOfDelivery());
    }

    @Test
    void updateReservation() throws NotFoundException {
        Reservation reservation = reservationService
            .updateReservation(testReservation.getId(), testReservationDto);

        assertEquals(testReservation.getId(), reservation.getId());
        assertEquals(testReservationDto.getTimeOfPickup(), reservation.getTimeOfPickup());
        assertEquals(testReservationDto.getTimeOfDelivery(), reservation.getTimeOfDelivery());
    }
}