package nl.tudelft.oopp.group39.reservation.model;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class Reservation {
    private Integer id;
    private String timeOfDelivery;
    private String timeOfPickup;
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
        ArrayNode reservationAmounts
    ) {
        this.id = id;
        this.timeOfDelivery = timeOfDelivery;
        this.timeOfPickup = timeOfPickup;
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

    public ArrayNode getReservationAmounts() {
        return reservationAmounts;
    }
}
