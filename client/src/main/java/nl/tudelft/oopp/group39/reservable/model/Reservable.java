package nl.tudelft.oopp.group39.reservable.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Reservable {
    private Long id;
    private Double price;
    private Long building;

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

    public Long getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public Long getBuilding() {
        return building;
    }
}
