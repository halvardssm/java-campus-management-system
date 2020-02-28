package nl.tudelft.oopp.group39.building.entities;


import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = Building.TABLE_NAME)
public class Building {
    public static final String TABLE_NAME = "buildings";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String location;

    private String description;

    private LocalTime open;

    private LocalTime closed;

    //opening times (open & closed)

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

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getOpen() {
        return open;
    }

    public LocalTime getClosed() {
        return closed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOpen(LocalTime open) {
        this.open = open;
    }

    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Building building = (Building) o;

        boolean equals = (building.location.contentEquals(location)) && (building.name.contentEquals(name));
        equals = equals && (building.description.contentEquals(description)) && (building.open == open);
        equals = equals && (building.closed == closed) && (id == building.id);
        return equals;
    }
}
