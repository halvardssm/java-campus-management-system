package nl.tudelft.oopp.group39.reservation.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;

@Embeddable
public class ReservationAmountKey implements Serializable {

    @Column(name = Reservation.MAPPED_NAME)
    Integer reservation;

    @Column(name = Reservable.MAPPED_NAME)
    Integer reservable;

    public ReservationAmountKey() {
    }

    /**
     * Constructor.
     *
     * @param reservation the reservation
     * @param reservable  the reservable
     */
    public ReservationAmountKey(Integer reservation, Integer reservable) {
        setReservation(reservation);
        setReservable(reservable);
    }

    public Integer getReservation() {
        return reservation;
    }

    public void setReservation(Integer reservation) {
        this.reservation = reservation;
    }

    public Integer getReservable() {
        return reservable;
    }

    public void setReservable(Integer reservable) {
        this.reservable = reservable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationAmountKey)) {
            return false;
        }
        ReservationAmountKey that = (ReservationAmountKey) o;
        return Objects.equals(getReservation(), that.getReservation())
            && Objects.equals(getReservable(), that.getReservable());
    }
}

