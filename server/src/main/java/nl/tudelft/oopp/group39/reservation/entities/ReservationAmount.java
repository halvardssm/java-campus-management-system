package nl.tudelft.oopp.group39.reservation.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;

@Entity
@Table(name = ReservationAmount.TABLE_NAME)
public class ReservationAmount extends AbstractEntity<ReservationAmount, ReservationAmountDto> {
    public static final String TABLE_NAME = Reservation.TABLE_NAME + "_" + Reservable.TABLE_NAME;
    public static final String COL_AMOUNT = "amount";
    public static final String COL_RESERVATION = "reservation";
    public static final String COL_RESERVABLE = "reservable";

    private Integer amount;
    @ManyToOne
    @JoinColumn(name = Reservation.MAPPED_NAME)
    private Reservation reservation;
    @ManyToOne
    @JoinColumn(name = Reservable.MAPPED_NAME)
    private Reservable reservable;

    public ReservationAmount() {
    }

    /**
     * Constructor for ReservationAmount.
     *
     * @param id     the id
     * @param amount the amount of the item
     */
    public ReservationAmount(
        Long id,
        Integer amount,
        Reservation reservation,
        Reservable reservable
    ) {
        setId(id);
        setAmount(amount);
        setReservation(reservation);
        setReservable(reservable);
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    /**
     * Converts the object to dto for JSON serializing.
     *
     * @return the converted object
     */
    @Override
    public ReservationAmountDto toDto() {
        return new ReservationAmountDto(
            getId(),
            getAmount(),
            getReservable() == null
                ? null : getReservable().getId()
        );
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