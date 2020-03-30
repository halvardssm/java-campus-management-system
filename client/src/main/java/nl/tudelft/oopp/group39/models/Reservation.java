package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Reservation {
    private Integer id;
    private String timeOfDelivery;
    private String timeOfPickup;
    private ArrayNode reservationAmounts;

    public Reservation() {

    }

    public Reservation(Integer id, String timeOfDelivery, String timeOfPickup, ArrayNode reservationAmounts) {
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

    public boolean isBike(int bikeId) {
        for (JsonNode reservationAmount : reservationAmounts) {
            if (reservationAmount.get("reservable").get("id").asInt() == bikeId) {
                return true;
            }
        }
        return false;
    }
}
