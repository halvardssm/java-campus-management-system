package nl.tudelft.oopp.group39.reservation.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ReservationDto {
    private LocalDateTime timeOfPickup;
    private LocalDateTime timeOfDelivery;
    private String user;
    private Long room;
    private Set<ReservationAmountDto> reservationAmounts = new HashSet<>();

    public ReservationDto() {
    }

    /**
     * Constructor for the ReservationDto.
     *
     * @param timeOfPickup       the time of pickup
     * @param timeOfDelivery     the time of delivery, nullable
     * @param user               the username connected
     * @param room               the room id connected
     * @param reservationAmounts set of {@link ReservationAmountDto}
     */
    public ReservationDto(
        LocalDateTime timeOfPickup,
        LocalDateTime timeOfDelivery,
        String user,
        Long room,
        Set<ReservationAmountDto> reservationAmounts
    ) {
        this.timeOfPickup = timeOfPickup;
        this.timeOfDelivery = timeOfDelivery;
        this.user = user;
        this.room = room;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Set<ReservationAmountDto> getReservationAmounts() {
        return reservationAmounts;
    }

    public void setReservationAmounts(Set<ReservationAmountDto> reservationAmounts) {
        this.reservationAmounts = reservationAmounts;
    }
}
