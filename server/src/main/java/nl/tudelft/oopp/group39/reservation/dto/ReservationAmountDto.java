package nl.tudelft.oopp.group39.reservation.dto;

import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

public class ReservationAmountDto extends AbstractDto<ReservationAmount, ReservationAmountDto> {
    private Integer amount;
    private Long reservable;

    /**
     * The ReservationAmountDto constructor.
     */
    public ReservationAmountDto() {
    }

    /**
     * The ReservationAmountDto constructor.
     *
     * @param id         the id
     * @param amount     the amount reserved
     * @param reservable the reservable
     */
    public ReservationAmountDto(Long id, Integer amount, Long reservable) {
        setId(id);
        setAmount(amount);
        setReservable(reservable);
    }

    /**
     * Gets the amount of the reservation.
     *
     * @return the amount of the reservation
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Changes the amount of the reservation.
     *
     * @param amount the new amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * Gets the reservables for the reservation.
     *
     * @return the reservable
     */
    public Long getReservable() {
        return reservable;
    }

    /**
     * Changes the reservable for the reservation.
     *
     * @param reservable the new reservables
     */
    public void setReservable(Long reservable) {
        this.reservable = reservable;
    }

    /**
     * Changes ReservationAmountDto to ReservationAmount object.
     *
     * @return a ReservationAmount object
     */
    @Override
    public ReservationAmount toEntity() {
        return new ReservationAmount(
            getId(),
            getAmount(),
            null,
            Utils.idToEntity(getReservable(), Reservable.class)
        );
    }
}
