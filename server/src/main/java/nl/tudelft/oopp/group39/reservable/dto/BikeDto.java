package nl.tudelft.oopp.group39.reservable.dto;

import java.util.Set;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

public class BikeDto extends ReservableDto {

    private BikeType bikeType;

    public BikeDto() {
    }

    public BikeDto(
        BikeType bikeType,
        Double price,
        Integer building,
        Set<ReservationAmountDto> reservations
    ) {
        super(price, building, reservations);
        this.bikeType = bikeType;
        setBikeType(bikeType != null ? bikeType : BikeType.CITY);
    }

    public BikeType getBikeType() {
        return bikeType;
    }

    public void setBikeType(BikeType bikeType) {
        this.bikeType = bikeType;
    }
}
