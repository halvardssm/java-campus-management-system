package nl.tudelft.oopp.group39.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private Integer id;
    private String timeOfDelivery;
    private String timeOfPickup;
    private Long room;
    private ArrayNode reservationAmounts;
    private String user;
    @JsonIgnore
    private List<ReservableNode> reservables;

    /**
     * Creates a reservation.
     */
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
        ArrayNode reservationAmounts,
        String user
    ) {
        this.id = id;
        this.timeOfDelivery = timeOfDelivery;
        this.timeOfPickup = timeOfPickup;
        this.room = room;
        this.reservationAmounts = reservationAmounts;
        this.user = user;
    }

    /**
     * Gets the id of the reservation.
     *
     * @return the id of the reservation
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the time of delivery for the reservation.
     *
     * @return the time of delivery for the reservation
     */
    public String getTimeOfDelivery() {
        return timeOfDelivery;
    }

    /**
     * Gets the pick up time for the reservation.
     *
     * @return the pick up time for the reservation
     */
    public String getTimeOfPickup() {
        return timeOfPickup;
    }

    /**
     * Gets the room for the reservation.
     *
     * @return the room for the reservation
     */
    public Long getRoom() {
        return room;
    }

    /**
     * Gets the reservable and corresponding amounts.
     *
     * @return the reservables and the corresponding amounts
     */
    public ArrayNode getReservationAmounts() {
        return reservationAmounts;
    }

    /**
     * Gets the reservable.
     *
     * @return the reservable
     */
    public List<Long> getReservables() {
        List<Long> reservables = new ArrayList<>();
        for (JsonNode reservationAmount : reservationAmounts) {
            Long reservable = reservationAmount.get("reservable").asLong();
            reservables.add(reservable);
        }
        return reservables;
    }

    /**
     * Gets the pick up time for the reservation.
     *
     * @return the pick up time for the reservation
     */
    public LocalDateTime getPickupTime() {
        return LocalDateTime.parse(timeOfPickup.replace(" ", "T"));
    }

    /**
     * Gets the delivery time for the reservation.
     *
     * @return the delivery time for the reservation
     */
    public LocalDateTime getDeliveryTime() {
        return LocalDateTime.parse(timeOfDelivery.replace(" ", "T"));
    }

    /**
     * Gets the delivery time for the reservation.
     *
     * @return the delivery time for the reservation
     */
    public List<ReservableNode> getReservationItems() {
        return reservables;
    }

    /**
     * Gets the user for the reservation.
     *
     * @return the user for the reservation
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets reservation amount.
     *
     * @param reservationAmounts the value to be set as
     */
    public void setReservationAmounts(ArrayNode reservationAmounts) {
        this.reservationAmounts = reservationAmounts;
    }
}
