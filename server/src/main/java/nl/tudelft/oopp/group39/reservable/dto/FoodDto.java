package nl.tudelft.oopp.group39.reservable.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.util.Set;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.reservable.entities.Food;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;

public class FoodDto extends ReservableDto {

    private String name;
    private String description;

    public FoodDto() {
    }

    /**
     * Creates a FoodDto object.
     *
     * @param name name of the food reservable
     * @param description the description of said reservable
     * @param price price of the reservable
     * @param building building id associated with the reservable
     * @param reservations reservations associated with the reservable (in Dto form)
     */
    public FoodDto(
        String name,
        String description,
        Double price,
        Long building,
        Set<ReservationAmountDto> reservations
    ) {
        super(price, building, initSet(reservations));
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Reservable toEntity() {

        return new Food(
            getName(),
            getDescription(),
            getPrice(),
            Utils.idToEntity(getBuilding(), Building.class),
            Utils.setDtoToEntity(getReservations())
        );
    }
}
