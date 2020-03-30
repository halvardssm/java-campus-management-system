package nl.tudelft.oopp.group39.reservation.dto;

import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

public class ReservationAmountDto extends AbstractDto<ReservationAmount, ReservationAmountDto> {
    private Integer amount;
    private Long reservable;

    public ReservationAmountDto() {
    }

    /**
     * Constructor.
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getReservable() {
        return reservable;
    }

    public void setReservable(Long reservable) {
        this.reservable = reservable;
    }

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
