package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.JsonNode;

public class Reservable {
    private Integer id;
    private double price;
    private JsonNode building;

    public Reservable() {

    }

    public Reservable(Integer id, double price, JsonNode building) {
        this.id = id;
        this.price = price;
        this.building = building;
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

    public long getBuildingId() {
        return this.getBuilding().get("id").asInt();
    }
}
