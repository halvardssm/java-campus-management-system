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
public class Bike extends Reservable<Bike, BikeDto> {
    public static final String TABLE_NAME = "bikes";
    public static final String MAPPED_NAME = "bike";
    public static final String COL_BIKE_TYPE = "bikeType";

    @Enumerated(EnumType.STRING)
    private BikeType bikeType;

    /**
     * The Bike constructor.
     */
    public Bike() {
    }

    /**
     * The Bike constructor.
     *
     * @param id           of the bike
     * @param bikeType     the bike type
     * @param price        of the item
     * @param building     where the bike is available.
     * @param reservations the reservation
     */
    public Bike(
        Long id,
        BikeType bikeType,
        Double price,
        Building building,
        Set<ReservationAmount> reservations
    ) {
        super(id, price, building, reservations);
        setBikeType(bikeType != null ? bikeType : BikeType.CITY);
    }

    public BikeType getBikeType() {
        return bikeType;
    }

    public void setBikeType(BikeType bikeType) {
        this.bikeType = bikeType;
    }

    /**
     * Changes the Bike to a BikeDto object.
     *
     * @return a BikeDto object
     */
    @Override
    public BikeDto toDto() {

        return new BikeDto(
            getId(),
            getBikeType(),
            getPrice(),
            Utils.entityToId(getBuilding()),
            Utils.setEntityToDto(getReservations())
        );
    }

    /**
     * Checks whether two bikes are equal.
     *
     * @param o the other object
     * @return true if the two bikes are equal, false otherwise
     */
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
