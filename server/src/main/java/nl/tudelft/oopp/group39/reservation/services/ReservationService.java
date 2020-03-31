package nl.tudelft.oopp.group39.reservation.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservable.services.ReservableService;
import nl.tudelft.oopp.group39.reservation.dao.ReservationDao;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;
import nl.tudelft.oopp.group39.reservation.dto.ReservationDto;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.reservation.repositories.ReservationRepository;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    private ReservationDao reservationDao;

    /**
     * List all reservations.
     *
     * @return a list of reservations {@link Reservation}.
     */
    public List<Reservation> listReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> filterReservations(Map<String, String> filters) {
        return reservationDao.reservationFilter(filters);
    }

    /**
     * Get an reservation.
     *
     * @return reservation by id {@link Reservation}.
     */
    public Reservation readReservation(Long id) throws NotFoundException {
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
    public Reservation createReservation(ReservationDto reservation)
        throws IllegalArgumentException, NotFoundException {
        User user = userService.readUser(reservation.getUser());

        Reservation reservation1 = new Reservation(
            null,
            reservation.getTimeOfPickup(),
            reservation.getTimeOfDelivery(),
            null,
            user,
            new HashSet<>()
        );

        if (reservation.getRoom() != null) {
            Room room = roomService.readRoom(reservation.getRoom());
            reservation1.setRoom(room);
        }

        Reservation reservation2 = reservationRepository.save(reservation1);

        for (ReservationAmountDto reservationAmountDto : reservation.getReservationAmounts()) {
            Reservable reservable
                = reservableService.readReservable(reservationAmountDto.getReservable());

            ReservationAmount reservationAmount = new ReservationAmount(
                null,
                reservationAmountDto.getAmount(),
                reservation2,
                reservable
            );
            reservation2.getReservationAmounts().add(
                reservationAmountService.createReservation(reservationAmount));
        }

        return readReservation(reservation2.getId());
    }

    /**
     * Update an reservation.
     *
     * @return the updated reservation {@link Reservation}.
     */
    public Reservation updateReservation(Long id, ReservationDto newReservation)
        throws NotFoundException {

        Set<ReservationAmount> reservationAmounts = new HashSet<>();

        if (newReservation != null) {
            for (ReservationAmountDto reservationAmountDto : newReservation.getReservationAmounts()
            ) {
                reservationAmounts.add(reservationAmountDto.getId() == null
                    ? reservationAmountService.createReservation(reservationAmountDto.toEntity())
                    : reservationAmountService.updateReservation(
                    reservationAmountDto.getId(), reservationAmountDto.toEntity())
                );
            }
        }

        return reservationRepository.findById(id)
            .map(reservation -> {
                reservation.setRoom(newReservation.getRoom() == null
                    ? null : roomService.readRoom(newReservation.getRoom()));
                reservation.setUser(newReservation.getUser() == null
                    ? null : userService.readUser(newReservation.getUser()));
                reservation.setTimeOfPickup(newReservation.getTimeOfPickup());
                reservation.setTimeOfDelivery(newReservation.getTimeOfDelivery());
                reservation.getReservationAmounts().addAll(reservationAmounts);

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
    public void deleteReservation(Long id) {
        reservationAmountService.deleteReservationAmountsByReservationId(id);

        reservationRepository.deleteById(id);
    }
}
