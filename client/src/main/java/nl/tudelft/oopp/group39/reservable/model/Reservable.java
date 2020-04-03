package nl.tudelft.oopp.group39.reservable.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Reservable {
    private Long id;
    private Double price;
    private Long building;

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
    public Reservable(Long id, double price, Long building) {
        this.id = id;
        this.price = price;
        this.building = building;
    }

    /**
     * Gets the id of the reservable.
     *
     * @return the id of the reservable
     */
    public Long getId() {
        return id;
    }

    public Double getPrice() {
    /**
     * Gets the price of the reservable.
     *
     * @return the price of the reservable
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Gets the building id.
     *
     * @return the building id
     */
    public Long getBuilding() {
        return building;
    }
}
