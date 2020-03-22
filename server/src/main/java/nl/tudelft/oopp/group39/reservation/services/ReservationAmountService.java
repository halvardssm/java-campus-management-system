package nl.tudelft.oopp.group39.reservation.services;

import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.reservation.repositories.ReservationAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationAmountService {
    public static final String EXCEPTION_RESERVATION_NOT_FOUND = "Reservation %d not found";

    @Autowired
    private ReservationAmountRepository reservationAmountRepository;

    /**
     * List all reservations.
     *
     * @return a list of reservations {@link Reservation}.
     */
    public List<ReservationAmount> listReservations() {
        return reservationAmountRepository.findAll();
    }

    /**
     * Get an reservation.
     *
     * @return reservation by id {@link Reservation}.
     */
    public ReservationAmount readReservation(Integer id) throws NotFoundException {
        return reservationAmountRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format(
                EXCEPTION_RESERVATION_NOT_FOUND,
                id
            )));
    }

    /**
     * Create an reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    public ReservationAmount createReservation(ReservationAmount reservation)
        throws IllegalArgumentException {
        return reservationAmountRepository.save(reservation);
    }

    /**
     * Update an reservation.
     *
     * @return the updated reservation {@link ReservationAmount}.
     */
    public ReservationAmount updateReservation(
        Integer id,
        ReservationAmount newReservation
    )
        throws NotFoundException {
        return reservationAmountRepository.findById(id)
            .map(reservation -> {
                newReservation.setId(id);
                reservation = newReservation;

                return reservationAmountRepository.save(reservation);
            })
            .orElseThrow(() -> new NotFoundException(String.format(
                EXCEPTION_RESERVATION_NOT_FOUND,
                id
            )));
    }

    /**
     * Delete an reservation {@link ReservationAmount}.
     */
    public void deleteReservation(Integer id) {
        reservationAmountRepository.deleteById(id);
    }
}
