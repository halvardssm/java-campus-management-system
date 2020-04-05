package nl.tudelft.oopp.group39.reservation.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.LocalDateTime;

public class Reservation {
    private Integer id;
    private String timeOfDelivery;
    private String timeOfPickup;
    private Long room;
    private ArrayNode reservationAmounts;

    public Reservation() {

    }

    /**
     * Creates a reservation.
     *
     * @param id                 the id of the reservation
     * @param timeOfDelivery     the time of delivery of the reservation
     * @param timeOfPickup       the time of pickup of the reservation
     * @param reservationAmounts an ArrayNode containing the reservable and corresponding amounts
     */
    public Reservation(
        Integer id,
        String timeOfDelivery,
        String timeOfPickup,
        Long room,
        ArrayNode reservationAmounts
    ) {
        this.id = id;
        this.timeOfDelivery = timeOfDelivery;
        this.timeOfPickup = timeOfPickup;
        this.room = room;
        this.reservationAmounts = reservationAmounts;
    }

    public Integer getId() {
        return id;
    }

    public String getTimeOfDelivery() {
        return timeOfDelivery;
    }

    public String getTimeOfPickup() {
        return timeOfPickup;
    }

    public Long getRoom() {
        return room;
    }

    public ArrayNode getReservationAmounts() {
        return reservationAmounts;
    }

    public Long getReservable() {
        JsonNode reservationAmount = reservationAmounts.get(0);
        return reservationAmount.get("reservable").asLong();
    }

    public LocalDateTime getPickupTime() {
        return LocalDateTime.parse(timeOfPickup.replace(" ", "T"));
    }

    public LocalDateTime getDeliveryTime() {
        return LocalDateTime.parse(timeOfDelivery.replace(" ", "T"));
    }
}
