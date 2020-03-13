package nl.tudelft.oopp.group39.bike.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.entities.Building;

@Entity
@Table(name = Bike.TABLE_NAME)
public class Bike {
    public static final String TABLE_NAME = "bike_bookings";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double cost;
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = TABLE_NAME + "_" + Building.TABLE_NAME,
        joinColumns = {
            @JoinColumn(name = "bike_id", referencedColumnName = "id",
                nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = "building_id", referencedColumnName = "id",
                nullable = false, updatable = false)})
    private Set<Building> buildings = new HashSet<>();

    public Bike() {
    }

    /**
     * Creates a Bike item.
     *
     * @param cost        the cost of the product as a decimal
     * @param name        the name of the bike
     * @param description the description of the bike
     * @param buildings   all the buildings where the bike is available
     */
    public Bike(Double cost, String name, String description, Set<Building> buildings) {
        setCost(cost);
        setName(name);
        setDescription(description);
        this.buildings.addAll(buildings != null ? buildings : new HashSet<>());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bike)) {
            return false;
        }
        Bike bike = (Bike) o;
        return Objects.equals(getId(), bike.getId())
            && Objects.equals(getCost(), bike.getCost())
            && Objects.equals(getName(), bike.getName())
            && Objects.equals(getDescription(), bike.getDescription())
            && getBuildings().equals(bike.getBuildings());
    }
}
