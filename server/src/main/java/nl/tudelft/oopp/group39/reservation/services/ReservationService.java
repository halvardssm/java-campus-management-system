package nl.tudelft.oopp.group39.reservation.services;

import java.util.HashSet;
import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservable.services.ReservableService;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.reservation.models.ReservationAmountDTO;
import nl.tudelft.oopp.group39.reservation.models.ReservationDTO;
import nl.tudelft.oopp.group39.reservation.repositories.ReservationRepository;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    public static final String EXCEPTION_RESERVATION_NOT_FOUND = "Reservation %d not found";

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationAmountService reservationAmountService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReservableService reservableService;
    @Autowired
    private RoomService roomService;

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
        return reservationRepository.findById(id)
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
    public Reservation createReservation(Reservation reservation) throws IllegalArgumentException {
        return reservationRepository.save(reservation);
    }

    /**
     * Create an reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    public Reservation createReservation(ReservationDTO reservation)
        throws IllegalArgumentException, NotFoundException {
        User user = userService.readUser(reservation.getUser());
        Room room = roomService.readRoom(reservation.getRoom());

        Reservation reservation1 = new Reservation(
            reservation.getTimeOfPickup(),
            reservation.getTimeOfDelivery(),
            room,
            user,
            new HashSet<>()
        );
        Reservation reservation2 = reservationRepository.save(reservation1);

        for (ReservationAmountDTO reservationAmountDto : reservation.getReservationAmounts()) {
            Reservable reservable
                = reservableService.readReservable(reservationAmountDto.getReservable());

            ReservationAmount reservationAmount = new ReservationAmount(
                reservationAmountDto.getAmount(),
                reservation2,
                reservable
            );

            reservationAmountService.createReservation(reservationAmount);
        }

        return readReservation(reservation2.getId());
    }

    /**
     * Update an reservation.
     *
     * @return the updated reservation {@link Reservation}.
     */
    public Reservation updateReservation(Integer id, Reservation newReservation)
        throws NotFoundException {
        return reservationRepository.findById(id)
            .map(reservation -> {
                newReservation.setId(id);
                reservation = newReservation;

                return reservationRepository.save(reservation);
            })
            .orElseThrow(() -> new NotFoundException(String.format(
                EXCEPTION_RESERVATION_NOT_FOUND,
                id
            )));
    }

    /**
     * Delete an reservation {@link Reservation}.
     */
    public void deleteReservation(Integer id) {
        reservationRepository.deleteById(id);
    }
}
