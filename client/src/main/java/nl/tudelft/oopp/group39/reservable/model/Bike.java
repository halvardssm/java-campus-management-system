package nl.tudelft.oopp.group39.reservable.model;

import com.fasterxml.jackson.databind.JsonNode;
import nl.tudelft.oopp.group39.reservable.model.Reservable;

public class Bike extends Reservable {
    private String bikeType;
    private String rentalDuration;

    /**
     * Creates a bike.
     */
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

    /**
     * Gets the bike type.
     *
     * @return the bike type
     */
    public String getBikeType() {
        return bikeType;
    }

    /**
     * Gets the duration how long a rental takes place.
     *
     * @return the rental duration
      */
    public String getRentalDuration() {
        return rentalDuration;
    }
}
