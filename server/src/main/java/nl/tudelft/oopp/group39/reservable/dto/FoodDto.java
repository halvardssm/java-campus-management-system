package nl.tudelft.oopp.group39.reservable.dto;

import java.util.Set;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;

public class FoodDto extends ReservableDto {

    private String name;
    private String description;

    public FoodDto() {
    }

    public FoodDto(
        String name,
        String description,
        Double price,
        Integer building,
        Set<ReservationAmountDto> reservations
    ) {
        super(price, building, reservations);
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
}
