package nl.tudelft.oopp.group39.reservable.entities;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.reservable.dto.FoodDto;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

@Entity
@Table(name = Food.TABLE_NAME)
public class Food extends Reservable<Food, FoodDto> {
    public static final String TABLE_NAME = "foods";
    public static final String MAPPED_NAME = "food";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";

    private String name;
    private String description;

    /**
     * The Food constructor.
     */
    public Food() {
    }

    /**
     * The Food constructor.
     *
     * @param id          of the item
     * @param name        of the item
     * @param description of the item
     * @param price       of the item
     * @param building    where the item is available
     * @param reservation the reservation
     */
    public Food(
        Long id,
        String name,
        String description,
        Double price,
        Building building,
        Set<ReservationAmount> reservation
    ) {
        super(id, price, building, reservation);
        setName(name);
        setDescription(description);
    }

    /**
     * Gets the name of the food.
     *
     * @return the name of the food
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the food.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the food.
     *
     * @return the description of the food
     */
    public String getDescription() {
        return description;
    }

    /**
     * Changes the description of the food.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Converts a food entity to dto for JSON serializing.
     *
     * @return the converted FoodDto object
     */
    @Override
    public FoodDto toDto() {

        return new FoodDto(
            getId(),
            getName(),
            getDescription(),
            getPrice(),
            getBuilding() == null ? null : getBuilding().getId(),
            Utils.setEntityToDto(getReservations())
        );
    }

    /**
     * Checks whether two foods are equal.
     *
     * @param o the other object
     * @return true if the two foods are equal, false otherwise
     */
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
