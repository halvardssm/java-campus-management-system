package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Bike {

    private Integer id;
    private double price;
    private JsonNode building;
    private String bikeType;
    private String rentalDuration;

    public Bike() {

    }

    public Bike(Integer id, double price, JsonNode building, String bikeType, String rentalDuration) {
        this.id = id;
        this.price = price;
        this.building = building;
        this.bikeType = bikeType;
        this.rentalDuration = rentalDuration;
    }

    public Integer getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public JsonNode getBuilding() {
        return building;
    }

    public String getBikeType() {
        return bikeType;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }
}
