package nl.tudelft.oopp.group39.reservation.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.reservation.dto.ReservationDto;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;

@Entity
@Table(name = Reservation.TABLE_NAME)
public class Reservation extends AbstractEntity<Reservation, ReservationDto> {
    public static final String TABLE_NAME = "reservations";
    public static final String MAPPED_NAME = "reservation";
    public static final String COL_TIME_OF_PICKUP = "timeOfPickup";
    public static final String COL_TIME_OF_DELIVERY = "timeOfDelivery";
    public static final String COL_ROOM = "room";
    public static final String COL_USER = "user";
    public static final String COL_RESERVATION_AMOUNTS = "reservationAmounts";

    private LocalDateTime timeOfPickup;
    private LocalDateTime timeOfDelivery;
    @ManyToOne
    @JoinColumn(name = Room.MAPPED_NAME)
    private Room room;
    @ManyToOne
    @JoinColumn(name = User.MAPPED_NAME) //TODO change to id
    private User user;
    @OneToMany(mappedBy = MAPPED_NAME)
    private Set<ReservationAmount> reservationAmounts = new HashSet<>();

    /**
     * The Reservation constructor.
     */
    public Reservation() {
    }

    /**
     * The Reservation constructor.
     *
     * @param id                 the id
     * @param timeOfPickup       the time of the pickup
     * @param timeOfDelivery     the time of the delivery (null for food)
     * @param room               the room
     * @param user               the user
     * @param reservationAmounts all items in order
     */
    public Reservation(
        Long id,
        LocalDateTime timeOfPickup,
        LocalDateTime timeOfDelivery,
        Room room,
        User user,
        Set<ReservationAmount> reservationAmounts
    ) {
        setId(id);
        setTimeOfPickup(timeOfPickup);
        setTimeOfDelivery(timeOfDelivery);
        setRoom(room);
        setUser(user);
        getReservationAmounts().addAll(initSet(reservationAmounts));
    }

    /**
     * Gets the pick up time of the reservation.
     *
     * @return the pick up time
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
     * Gets the delivery time of the reservation.
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
     * Gets the room of the reservation.
     *
     * @return the room of the reservation
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Changes the room of the reservation.
     *
     * @param room the new room of the reservation
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Gets the user of the reservation.
     *
     * @return the user of the reservation
     */
    public User getUser() {
        return user;
    }

    /**
     * Changes the user of the reservation.
     *
     * @param user the new user of the reservation
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the amount of items for the reservation.
     *
     * @return the amount of items in the reservation
     */
    public Set<ReservationAmount> getReservationAmounts() {
        return reservationAmounts;
    }

    /**
     * Changes the amount of items in the reservation.
     *
     * @param reservables the new set of items for the reservation
     */
    public void setReservationAmounts(Set<ReservationAmount> reservables) {
        this.reservationAmounts = reservables;
    }

    /**
     * Converts the object to dto for JSON serializing.
     *
     * @return the converted object
     */
    @Override
    public ReservationDto toDto() {
        return new ReservationDto(
            getId(),
            getTimeOfPickup(),
            getTimeOfDelivery(),
            getUser().getUsername(),
            room == null ? null : getRoom().getId(),
            Utils.setEntityToDto(getReservationAmounts())
        );
    }

    /**
     * Checks whether two reservations are equal.
     *
     * @param o the other object
     * @return true if the two reservations are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getTimeOfPickup(), that.getTimeOfPickup())
            && Objects.equals(getTimeOfDelivery(), that.getTimeOfDelivery())
            && Objects.equals(getRoom(), that.getRoom())
            && Objects.equals(getUser(), that.getUser())
            && Objects.equals(getReservationAmounts(), that.getReservationAmounts());
    }
}
