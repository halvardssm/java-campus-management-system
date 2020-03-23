package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.JsonNode;


public class Food extends Reservable {

    private String name;
    private String description;

    public Food() {
        super();
    }

    public Food(Integer id, String name, String desc, double price, JsonNode building) {
        this.name = name;
        this.description = desc;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
