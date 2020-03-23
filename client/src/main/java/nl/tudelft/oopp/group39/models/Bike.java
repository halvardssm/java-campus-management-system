package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Bike extends Reservable {

    private String bikeType;
    private String rentalDuration;

    public Bike() {
        super();
    }

    /**
     * Creates a bike.
     *
     * @param id             of the bike
     * @param price          of the bike per hour
     * @param building       where the bike is available
     * @param bikeType       type of the bike
     * @param rentalDuration for how long the bike can be rented
     */
    public Bike(
        Integer id,
        double price,
        JsonNode building,
        String bikeType,
        String rentalDuration
    ) {
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