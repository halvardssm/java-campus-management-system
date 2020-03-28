package nl.tudelft.oopp.group39.reservation.dto;

import org.springframework.stereotype.Component;

@Component
public class ReservationAmountDto {
    private Integer amount;
    private Long reservable;

    public ReservationAmountDto() {
    }

    public ReservationAmountDto(Integer amount, Long reservable) {
        this.amount = amount;
        this.reservable = reservable;
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
}
