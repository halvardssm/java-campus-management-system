package nl.tudelft.oopp.group39.reservation.dto;

import org.springframework.stereotype.Component;

@Component
public class ReservationAmountDto {
    private Integer amount;
    private Integer reservable;

    public ReservationAmountDto() {
    }

    public ReservationAmountDto(Integer amount, Integer reservable) {
        this.amount = amount;
        this.reservable = reservable;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getReservable() {
        return reservable;
    }

    public void setReservable(Integer reservable) {
        this.reservable = reservable;
    }
}
