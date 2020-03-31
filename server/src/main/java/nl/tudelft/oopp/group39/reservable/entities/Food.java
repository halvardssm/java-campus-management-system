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
public class Food extends Reservable {
    public static final String TABLE_NAME = "foods";
    public static final String MAPPED_NAME = "food";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";

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
