package nl.tudelft.oopp.group39.reservation.models;

import org.springframework.stereotype.Component;

@Component
public class ReservationAmountDTO {
    private Integer amount;
    private Integer reservable;

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
