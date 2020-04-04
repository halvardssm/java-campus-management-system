package nl.tudelft.oopp.group39.reservation.services;

import java.util.List;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.reservation.repositories.ReservationAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationAmountService {

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
    public ReservationAmount readReservation(Long id) throws NotFoundException {
        return reservationAmountRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Reservation.MAPPED_NAME, id));
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
        Long id,
        ReservationAmount newReservation
    ) {
        return reservationAmountRepository.findById(id)
            .map(reservation -> {
                newReservation.setId(id);
                reservation = newReservation;

                return reservationAmountRepository.save(reservation);
            })
            .orElseThrow(() -> new NotFoundException(Reservation.MAPPED_NAME, id));
    }

    /**
     * Delete an reservation {@link ReservationAmount}.
     */
    public void deleteReservation(Long id) {
        reservationAmountRepository.deleteById(id);
    }

    /**
     * Delete all reservation amounts by reservation id {@link ReservationAmount}.
     */
    public void deleteReservationAmountsByReservationId(Long id) {
        reservationAmountRepository.deleteByReservationId(id);
    }
}
