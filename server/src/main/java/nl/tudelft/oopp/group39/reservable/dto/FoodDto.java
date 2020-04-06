package nl.tudelft.oopp.group39.reservable.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.util.Set;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.reservable.entities.Food;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;

public class FoodDto extends ReservableDto<Food, FoodDto> {
    private String name;
    private String description;

    /**
     * Creates a FoodDto object.
     */
    public FoodDto() {
    }

    /**
     * Creates a FoodDto object.
     *
     * @param id           the id
     * @param name         name of the food reservable
     * @param description  the description of said reservable
     * @param price        price of the reservable
     * @param building     building id associated with the reservable
     * @param reservations reservations associated with the reservable (in Dto form)
     */
    public FoodDto(
        Long id,
        String name,
        String description,
        Double price,
        Long building,
        Set<ReservationAmountDto> reservations
    ) {
        super(id, price, building, initSet(reservations));
        this.name = name;
        this.description = description;
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
     * Changes the FoodDto to a Food object.
     *
     * @return a Food object
     */
    @Override
    public Food toEntity() {
        return new Food(
            getId(),
            getName(),
            getDescription(),
            getPrice(),
            getBuilding() == null
                ? null : Utils.idToEntity(getBuilding(), Building.class),
            Utils.setDtoToEntity(getReservations())
        );
    }
}
