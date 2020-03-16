package nl.tudelft.oopp.group39.entities;

import java.time.LocalTime;

public class Building {

    private long id;

    private String name;

    private String location;

    private String description;

    private LocalTime open;

    private LocalTime closed;


    public Building() {
    }

    public Building(String name, String location, String description, LocalTime open, LocalTime closed) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.open = open;
        this.closed = closed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalTime getOpen() {
        return open;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public LocalTime getClosed() {
        return closed;
    }

    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

}
