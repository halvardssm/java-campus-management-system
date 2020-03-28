package nl.tudelft.oopp.group39.reservable.entities;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.reservable.dto.BikeDto;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

@Entity
@Table(name = Bike.TABLE_NAME)
public class Bike extends Reservable {
    public static final String TABLE_NAME = "bikes";
    public static final String MAPPED_NAME = "bike";
    public static final String COL_BIKE_TYPE = "bikeType";

    @Enumerated(EnumType.STRING)
    private BikeType bikeType;

    public BikeType getBikeType() {
        return bikeType;
    }

    public void setBikeType(BikeType bikeType) {
        this.bikeType = bikeType;
    }

    public Bike() {
    }

    /**
     * The constructor of Bike.
     *
     * @param bikeType     the bike type
     * @param price        of the item
     * @param building     where the bike is available.
     * @param reservations the reservation
     */
    public Bike(
        BikeType bikeType,
        Double price,
        Building building,
        Set<ReservationAmount> reservations
    ) {
        super(price, building, reservations);
        setBikeType(bikeType != null ? bikeType : BikeType.CITY);
    }

    @Override
    public BikeDto toDto() {

        return new BikeDto(
            getBikeType(),
            getPrice(),
            getBuilding().getId(),
            Utils.setEntityToDto(getReservations())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bike)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Bike bike = (Bike) o;
        return getBikeType() == bike.getBikeType();
    }
}
