package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.JsonNode;


public class Food {
    private Integer id;
    private String name;
    private String description;
    private double price;
    private JsonNode building;

    public Food() {

    }

    public Food(Integer id, String name, String desc, double price, JsonNode building) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.price = price;
        this.building = building;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
