package nl.tudelft.oopp.group39.reservation.entities;

import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;

@Entity
public class ReservationAmount {

    @EmbeddedId
    ReservationAmountKey id;

    @ManyToOne
    @MapsId(Reservation.MAPPED_NAME)
    @JoinColumn(name = Reservation.MAPPED_NAME)
    Reservation reservation;

    @ManyToOne
    @MapsId(Reservable.MAPPED_NAME)
    @JoinColumn(name = Reservable.MAPPED_NAME)
    Reservable reservable;

    Integer amount;

    public ReservationAmount() {
    }

    public ReservationAmountKey getId() {
        return id;
    }

    public void setId(ReservationAmountKey id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservable getReservable() {
        return reservable;
    }

    public void setReservable(Reservable reservable) {
        this.reservable = reservable;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationAmount)) {
            return false;
        }
        ReservationAmount that = (ReservationAmount) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getReservation(), that.getReservation())
            && Objects.equals(getReservable(), that.getReservable())
            && Objects.equals(getAmount(), that.getAmount());
    }
}