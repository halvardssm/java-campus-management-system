package nl.tudelft.oopp.group39.reservable.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Food extends Reservable {
    private String name;
    private String description;

    /**
     * Creates food.
     */
    public Food() {
        super();
    }

    /**
     * Creates food.
     *
     * @param id       the id of the food
     * @param name     the name of the food
     * @param desc     a (small) description of the food
     * @param price    the price of the food
     * @param building the building where the food is
     */
    public Food(Integer id, String name, String desc, double price, JsonNode building) {
        this.name = name;
        this.description = desc;
    }

    /**
     * Gets the name of the food.
     *
     * @return the name of the food
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the food.
     *
     * @return the description of the food
     */
    public String getDescription() {
        return description;
    }
}
