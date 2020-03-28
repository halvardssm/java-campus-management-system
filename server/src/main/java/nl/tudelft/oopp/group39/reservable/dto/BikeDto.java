package nl.tudelft.oopp.group39.reservable.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.util.Set;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;

public class BikeDto extends ReservableDto {

    private BikeType bikeType;

    public BikeDto() {
    }

    /**
     * Creates a BikeDto object.
     *
     * @param bikeType type of the bike
     * @param price price of the bike
     * @param building building id of the bike
     * @param reservations reservation associated with the bike
     */
    public BikeDto(
        BikeType bikeType,
        Double price,
        Long building,
        Set<ReservationAmountDto> reservations
    ) {
        super(price, building, initSet(reservations));
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
