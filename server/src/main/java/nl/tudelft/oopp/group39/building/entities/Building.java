package nl.tudelft.oopp.group39.building.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
    private LocalTime open;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")
    private LocalTime closed;

    //opening times (open & closed)

    public Building() {
    }

    /**
     * Constructor. TODO Sven
     *
     * @param name        name
     * @param location    location
     * @param description description
     * @param open        open
     * @param closed      closed
     */
    public Building(
        String name,
        String location,
        String description,
        LocalTime open,
        LocalTime closed
    ) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        Building building = (Building) o;
        return getId() == building.getId()
            && Objects.equals(getName(), building.getName())
            && Objects.equals(getLocation(), building.getLocation())
            && Objects.equals(getDescription(), building.getDescription())
            && Objects.equals(getOpen(), building.getOpen())
            && Objects.equals(getClosed(), building.getClosed());
    }
}
