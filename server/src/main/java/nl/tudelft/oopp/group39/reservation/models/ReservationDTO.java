package nl.tudelft.oopp.group39.reservation.models;

import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ReservationDTO {
    private LocalDateTime timeOfPickup;
    private LocalDateTime timeOfDelivery;
    private String user;
    private Integer room;
    private Set<ReservationAmountDTO> reservationAmounts;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Set<ReservationAmountDTO> getReservationAmounts() {
        return reservationAmounts;
    }

    public void setReservationAmounts(Set<ReservationAmountDTO> reservationAmounts) {
        this.reservationAmounts = reservationAmounts;
    }
}
