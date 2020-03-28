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

    public Reservation() {
    }

    /**
     * Constructor of Reservation.
     *
     * @param timeOfPickup       the time of the pickup
     * @param timeOfDelivery     the time of the delivery (null for food)
     * @param room               the room
     * @param user               the user
     * @param reservationAmounts all items in order
     */
    public Reservation(
        LocalDateTime timeOfPickup,
        LocalDateTime timeOfDelivery,
        Room room,
        User user,
        Set<ReservationAmount> reservationAmounts
    ) {
        setTimeOfPickup(timeOfPickup);
        setTimeOfDelivery(timeOfDelivery);
        setRoom(room);
        setUser(user);
        this.reservationAmounts.addAll(initSet(reservationAmounts));
    }

    public LocalDateTime getTimeOfPickup() {
        return timeOfPickup;
    }

    public void setTimeOfPickup(LocalDateTime timeOfPickup) {
        this.timeOfPickup = timeOfPickup;
    }

    public LocalDateTime getTimeOfDelivery() {
        return timeOfDelivery;
    }

    public void setTimeOfDelivery(LocalDateTime timeOfDelivery) {
        this.timeOfDelivery = timeOfDelivery;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ReservationAmount> getReservationAmounts() {
        return reservationAmounts;
    }

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
            getTimeOfPickup(),
            getTimeOfDelivery(),
            getUser().getUsername(),
            getRoom().getId(),
            Utils.setEntityToDto(getReservationAmounts())
        );
    }

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
