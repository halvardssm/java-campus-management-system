package nl.tudelft.oopp.group39.entities;

import com.google.gson.JsonObject;

public class Food {
    private int id;
    private String name;
    private String description;
    private double price;
    private JsonObject building;

    public Food(int id, String name, String desc, double price, JsonObject building) {
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

    public JsonObject getBuilding() {
        return building;
    }
}
