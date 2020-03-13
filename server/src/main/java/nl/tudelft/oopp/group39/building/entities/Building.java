package nl.tudelft.oopp.group39.building.entities;


import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.food.entities.Food;

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
    @ManyToMany(mappedBy = TABLE_NAME, fetch = FetchType.LAZY)
    private Set<Food> food = new HashSet<>();

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

    public Set<Food> getFood() {
        return food;
    }

    public void setFood(Set<Food> food) {
        this.food = food;
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
