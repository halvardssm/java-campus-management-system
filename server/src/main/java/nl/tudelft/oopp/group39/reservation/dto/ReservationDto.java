package nl.tudelft.oopp.group39.reservation.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class ReservationDto extends AbstractDto<Reservation, ReservationDto> {
    private LocalDateTime timeOfPickup;
    private LocalDateTime timeOfDelivery;
    private String user;
    private Long room;
    private Set<ReservationAmountDto> reservationAmounts = new HashSet<>();

    /**
     * Constructor for the ReservationDto.
     */
    public ReservationDto() {
    }

    /**
     * Constructor for the ReservationDto.
     *
     * @param id                 the id
     * @param timeOfPickup       the time of pickup
     * @param timeOfDelivery     the time of delivery, nullable
     * @param user               the username connected
     * @param room               the room id connected
     * @param reservationAmounts set of {@link ReservationAmountDto}
     */
    public ReservationDto(
        Long id,
        LocalDateTime timeOfPickup,
        LocalDateTime timeOfDelivery,
        String user,
        Long room,
        Set<ReservationAmountDto> reservationAmounts
    ) {
        setId(id);
        setTimeOfPickup(timeOfPickup);
        setTimeOfDelivery(timeOfDelivery);
        setUser(user);
        setRoom(room);
        getReservationAmounts().addAll(initSet(reservationAmounts));
    }

    /**
     * Gets the time when someone will pick up the reservation.
     *
     * @return the time when someone will pick up the reservation
     */
    public LocalDateTime getTimeOfPickup() {
        return timeOfPickup;
    }

    /**
     * Changes the pick up time of the reservation.
     *
     * @param timeOfPickup the new pick up time
     */
    public void setTimeOfPickup(LocalDateTime timeOfPickup) {
        this.timeOfPickup = timeOfPickup;
    }

    /**
     * Gets the time when the reservations are delivered.
     *
     * @return the delivery time
     */
    public LocalDateTime getTimeOfDelivery() {
        return timeOfDelivery;
    }

    /**
     * Changes the delivery time.
     *
     * @param timeOfDelivery the new delivery time
     */
    public void setTimeOfDelivery(LocalDateTime timeOfDelivery) {
        this.timeOfDelivery = timeOfDelivery;
    }

    /**
     * Gets the user who ordered the reservation.
     *
     * @return the username who ordered the reservation
     */
    public String getUser() {
        return user;
    }

    /**
     * Changes the user who ordered the reservation.
     *
     * @param user the new username
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the room for which the reservation holds.
     *
     * @return the room id
     */
    public Long getRoom() {
        return room;
    }

    /**
     * Changes the room for which the reservation holds.
     *
     * @param room the new room id
     */
    public void setRoom(Long room) {
        this.room = room;
    }

    /**
     * Gets the amount of reservations.
     *
     * @return the reservation amount
     */
    public Set<ReservationAmountDto> getReservationAmounts() {
        return reservationAmounts;
    }

    /**
     * Changes the reservation amounts.
     *
     * @param reservationAmounts the new set of amountReservations
     */
    public void setReservationAmounts(Set<ReservationAmountDto> reservationAmounts) {
        this.reservationAmounts = reservationAmounts;
    }

    /**
     * Changes the ReservationDto to a Reservation object.
     *
     * @return a Reservation object
     */
    @Override
    public Reservation toEntity() {
        return new Reservation(
            getId(),
            getTimeOfPickup(),
            getTimeOfDelivery(),
            Utils.idToEntity(getRoom(), Room.class),
            null,
            Utils.setDtoToEntity(getReservationAmounts())
        );
    }
}
