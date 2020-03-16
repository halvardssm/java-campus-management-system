package nl.tudelft.oopp.group39.reservation.services;

import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    public static final String EXCEPTION_RESERVATION_NOT_FOUND = "Reservation %d not found";

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * List all reservations.
     *
     * @return a list of reservations {@link Reservation}.
     */
    public List<Reservation> listReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Get an reservation.
     *
     * @return reservation by id {@link Reservation}.
     */
    public Reservation readReservation(Integer id) throws NotFoundException {
        return reservationRepository.findById(id).orElseThrow(()
            -> new NotFoundException(String.format(EXCEPTION_RESERVATION_NOT_FOUND, id)));
    }

    /**
     * Create an reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    public Reservation createReservation(Reservation reservation) throws IllegalArgumentException {
        return reservationRepository.save(reservation);
    }

    /**
     * Update an reservation.
     *
     * @return the updated reservation {@link Reservation}.
     */
    public Reservation updateReservation(Integer id, Reservation newReservation) throws NotFoundException {
        return reservationRepository.findById(id)
            .map(reservation -> {
                newReservation.setId(id);
                reservation = newReservation;

                return reservationRepository.save(reservation);
            })
            .orElseThrow(() -> new NotFoundException(String.format(EXCEPTION_RESERVATION_NOT_FOUND, id)));
    }

    /**
     * Delete an reservation {@link Reservation}.
     */
    public void deleteReservation(Integer id) {
        reservationRepository.deleteById(id);
    }
}
