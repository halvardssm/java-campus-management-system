package nl.tudelft.oopp.group39.reservation.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationAmountServiceTest extends AbstractTest {
    private final ReservationAmount testReservationAmount
        = new ReservationAmount(null, 1, null, null);

    @BeforeEach
    void setUp() {
        ReservationAmount reservationAmount
            = reservationAmountService.createReservation(testReservationAmount);
        testReservationAmount.setId(reservationAmount.getId());
    }

    @AfterEach
    void tearDown() {
        reservationAmountService.deleteReservation(testReservationAmount.getId());
        testReservationAmount.setId(null);
    }

    @Test
    void listReservations() {
        List<ReservationAmount> reservationAmounts = reservationAmountService.listReservations();
        assertEquals(1, reservationAmounts.size());
        assertEquals(testReservationAmount.getId(), reservationAmounts.get(0).getId());
        assertEquals(testReservationAmount.getAmount(), reservationAmounts.get(0).getAmount());
        assertEquals(
            testReservationAmount.getReservable(),
            reservationAmounts.get(0).getReservable()
        );
        assertEquals(
            testReservationAmount.getReservation(),
            reservationAmounts.get(0).getReservation()
        );
    }

    @Test
    void readReservation() throws NotFoundException {
        ReservationAmount reservationAmount
            = reservationAmountService.readReservation(testReservationAmount.getId());
        assertEquals(testReservationAmount.getId(), reservationAmount.getId());
        assertEquals(testReservationAmount.getAmount(), reservationAmount.getAmount());
        assertEquals(testReservationAmount.getReservable(), reservationAmount.getReservable());
        assertEquals(testReservationAmount.getReservation(), reservationAmount.getReservation());
    }

    @Test
    void deleteAndCreateReservation() {
        reservationAmountService.deleteReservation(testReservationAmount.getId());

        assertThrows(
            NotFoundException.class,
            () -> reservationAmountService.readReservation(testReservationAmount.getId())
        );

        ReservationAmount reservationAmount
            = reservationAmountService.createReservation(testReservationAmount);
        testReservationAmount.setId(reservationAmount.getId());

        assertNotNull(reservationAmount);
    }

    @Test
    void updateReservation() throws NotFoundException {
        testReservationAmount.setAmount(5);

        ReservationAmount reservationAmount = reservationAmountService
            .updateReservation(testReservationAmount.getId(), testReservationAmount);

        assertEquals(testReservationAmount.getAmount(), reservationAmount.getAmount());
    }
}