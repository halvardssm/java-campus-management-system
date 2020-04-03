package nl.tudelft.oopp.group39.reservable.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Reservable {
    private Integer id;
    private double price;
    private JsonNode building;

    /**
     * Creates a reservable.
     */
    public Reservable() {
    }

    /**
     * Creates a reservable.
     *
     * @param id       of the reservable
     * @param price    of the reservable
     * @param building where the reservable is available
     */
    public Reservable(Integer id, double price, JsonNode building) {
        this.id = id;
        this.price = price;
        this.building = building;
    }

    /**
     * Gets the id of the reservable.
     *
     * @return the id of the reservable
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the price of the reservable.
     *
     * @return the price of the reservable
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the building where the reservable is.
     *
     * @return the building
     */
    public JsonNode getBuilding() {
        return building;
    }

    /**
     * Gets the building id.
     *
     * @return the building id
     */
    public long getBuildingId() {
        return this.getBuilding().get("id").asInt();
    }
}
