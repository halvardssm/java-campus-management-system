package nl.tudelft.oopp.group39.reservable.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservableServiceTest extends AbstractTest {
    private final Reservable testReservable = new Reservable(null, 5.6, null, null);

    @BeforeEach
    void setUp() {
        Reservable reservable = reservableService.createReservable(testReservable);
        testReservable.setId(reservable.getId());
    }

    @AfterEach
    void tearDown() {
        reservableService.deleteReservable(testReservable.getId());
        testReservable.setId(null);
    }

    @Test
    void listReservables() {
        List<Reservable> reservables = reservableService.listReservables(new HashMap<>());

        assertEquals(1, reservables.size());
        assertEquals(testReservable.getId(), reservables.get(0).getId());
        assertEquals(testReservable.getPrice(), reservables.get(0).getPrice());
    }

    @Test
    void readReservable() throws NotFoundException {
        Reservable reservable = reservableService.readReservable(testReservable.getId());

        assertEquals(testReservable.getId(), reservable.getId());
        assertEquals(testReservable.getPrice(), reservable.getPrice());
    }

    @Test
    void deleteAndCreateReservable() {
        reservableService.deleteReservable(testReservable.getId());

        assertEquals(0, reservableService.listReservables(new HashMap<>()).size());

        Reservable reservable = reservableService.createReservable(testReservable);
        testReservable.setId(reservable.getId());

        assertEquals(testReservable.getId(), reservable.getId());
        assertEquals(testReservable.getPrice(), reservable.getPrice());
    }

    @Test
    void updateReservable() throws NotFoundException {
        testReservable.setPrice(6.7);

        Reservable reservable = reservableService.updateReservable(
            testReservable.getId(),
            testReservable
        );

        assertEquals(testReservable.getId(), reservable.getId());
        assertEquals(testReservable.getPrice(), reservable.getPrice());
    }
}