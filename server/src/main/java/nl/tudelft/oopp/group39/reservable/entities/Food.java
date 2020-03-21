package nl.tudelft.oopp.group39.reservable.entities;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

@Entity
@Table(name = Food.TABLE_NAME)
public class Food extends Reservable {
    public static final String TABLE_NAME = "foods";
    public static final String MAPPED_NAME = "food";

    private String name;
    private String description;

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

    public Food() {
    }

    /**
     * The constructor for Food.
     *
     * @param name        of the item
     * @param description of the item
     * @param building    where the item is available
     * @param price       of the item
     * @param reservation the reservation
     */
    public Food(
        String name,
        String description,
        Building building,
        Double price,
        Set<ReservationAmount> reservation
    ) {
        super(building, price, reservation);
        setName(name);
        setDescription(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Food)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Food food = (Food) o;
        return Objects.equals(getName(), food.getName())
            && Objects.equals(getDescription(), food.getDescription());
    }
}
