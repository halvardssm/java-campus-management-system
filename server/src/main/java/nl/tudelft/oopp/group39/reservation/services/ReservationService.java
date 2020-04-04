package nl.tudelft.oopp.group39.reservation.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
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
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
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

    /**
     * Method to filter reservations.
     *
     * @param filters the filter where you want to filter on
     * @return the filtered reservations
     */
    public List<Reservation> filterReservations(Map<String, String> filters) {
        return reservationDao.reservationFilter(filters);
    }

    /**
     * Get a reservation.
     *
     * @return reservation by id {@link Reservation}.
     */
    public Reservation readReservation(Long id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Reservation.MAPPED_NAME, id));
    }

    /**
     * Create a reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    public Reservation createReservation(Reservation reservation) throws IllegalArgumentException {
        return reservationRepository.save(reservation);
    }

    /**
     * Create a reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    public Reservation createReservation(ReservationDto reservation) {
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
     * Update a reservation.
     *
     * @return the updated reservation {@link Reservation}.
     */
    public Reservation updateReservation(Long id, ReservationDto newReservation) {
        Set<ReservationAmount> reservationAmounts = new HashSet<>();

        if (newReservation != null) {
            for (ReservationAmountDto reservationAmountDto : newReservation.getReservationAmounts()
            ) {
                reservationAmounts.add(reservationAmountDto.getId() == null
                    ? reservationAmountService.createReservation(
                    reservationAmountDto.toEntity())
                    : reservationAmountService.updateReservation(
                        reservationAmountDto.getId(),
                        reservationAmountDto.toEntity()
                    )
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
            .orElseThrow(() -> new NotFoundException(Reservation.MAPPED_NAME, id));
    }

    /**
     * Delete a reservation {@link Reservation}.
     */
    public void deleteReservation(Long id) {
        reservationAmountService.deleteReservationAmountsByReservationId(id);

        reservationRepository.deleteById(id);
    }
}
