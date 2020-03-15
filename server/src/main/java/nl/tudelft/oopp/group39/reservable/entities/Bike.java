package nl.tudelft.oopp.group39.reservable.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.reservable.enums.BikeType;

@Entity
@Table(name = Bike.TABLE_NAME)
public class Bike extends Reservable {
    public static final String TABLE_NAME = "bikes";

    @Enumerated(EnumType.STRING)
    private BikeType bikeType;
    @JsonFormat(pattern = Constants.FORMAT_TIME_SHORT)
    private LocalTime rentalDuration;

    /**
     * The constructor of Bike.
     *
     * @param building       where the bike is available.
     * @param price          of the item
     * @param bikeType       the bike type
     * @param rentalDuration duration of the rent
     * @param reservation    the reservation
     */
    public Bike(BikeType bikeType, LocalTime rentalDuration, Building building, Double price, Reservation reservation) {
        super(building, price, reservation);
        setBikeType(bikeType != null ? bikeType : BikeType.CITY);
        setRentalDuration(rentalDuration != null ? rentalDuration : LocalTime.parse("04:00:00"));
    }

    public BikeType getBikeType() {
        return bikeType;
    }

    public void setBikeType(BikeType bikeType) {
        this.bikeType = bikeType;
    }

    public LocalTime getRentalDuration() {
        return rentalDuration;
    }

    public Bike() {
    }

    public void setRentalDuration(LocalTime rentalDuration) {
        this.rentalDuration = rentalDuration;
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
        return getBikeType() == bike.getBikeType()
            && Objects.equals(getRentalDuration(), bike.getRentalDuration());
    }
}
