package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Bike extends Reservable {

    private String bikeType;
    private String rentalDuration;

    public Bike() {
        super();
    }

    public Bike(Integer id, double price, JsonNode building, String bikeType, String rentalDuration) {
        super(id, price, building);
        this.bikeType = bikeType;
        this.rentalDuration = rentalDuration;
    }

    public String getBikeType() {
        return bikeType;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }
}
