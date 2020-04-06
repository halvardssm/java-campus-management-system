package nl.tudelft.oopp.group39.reservable.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.reservable.dto.ReservableDto;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

@Entity
@Table(name = Reservable.TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(allowSetters = true, value = {Reservable.COL_RESERVATIONS})
@JsonIdentityInfo(generator = ObjectIdGenerators.None.class)
public class Reservable<E extends Reservable<E, D>, D extends ReservableDto<E, D>>
    extends AbstractEntity<E, D> {
    public static final String TABLE_NAME = "reservables";
    public static final String MAPPED_NAME = "reservable";
    public static final String COL_PRICE = "price";
    public static final String COL_BUILDING = "building";
    public static final String COL_RESERVATIONS = "reservations";

    private Double price;
    @ManyToOne
    @JoinColumn(name = Building.MAPPED_NAME) //TODO Change to id
    private Building building;
    @OneToMany(mappedBy = MAPPED_NAME)
    private Set<ReservationAmount> reservations = new HashSet<>();

    /**
     * The Reservable constructor.
     */
    public Reservable() {
    }

    /**
     * The Reservable constructor.
     *
     * @param id           the id
     * @param price        the price of the item
     * @param building     the building connected
     * @param reservations the reservations
     */
    public Reservable(
        Long id,
        Double price,
        Building building,
        Set<ReservationAmount> reservations
    ) {
        setId(id);
        setBuilding(building);
        setPrice(price);
        getReservations().addAll(initSet(reservations));
    }

    /**
     * Gets the building of the reservable.
     *
     * @return the building of the reservable
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Changes the building of the reservable.
     *
     * @param building the new building
     */
    public void setBuilding(Building building) {
        this.building = building;
    }

    /**
     * Gets the price of the reservable.
     *
     * @return the price of the reservable
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Changes the price of the reservable.
     *
     * @param price the new price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Gets the reservations for the reservable.
     *
     * @return a set with the reservables
     */
    public Set<ReservationAmount> getReservations() {
        return reservations;
    }

    /**
     * Changes the reservations of the reservable.
     *
     * @param reservations the new set of reservations.
     */
    public void setReservations(Set<ReservationAmount> reservations) {
        this.reservations = reservations;
    }

    /**
     * Converts the object to dto for JSON serializing.
     *
     * @return the converted dto of the object
     */
    @Override
    public D toDto() {
        Set<ReservationAmountDto> reservationAmountDtos = Utils.setEntityToDto(reservations);

        return (D) new ReservableDto(
            getId(),
            getPrice(),
            getBuilding().getId(),
            reservationAmountDtos
        );
    }

    /**
     * Checks whether two reservables are equal.
     *
     * @param o the other object
     * @return true if the two reservables are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservable)) {
            return false;
        }
        Reservable that = (Reservable) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getBuilding(), that.getBuilding())
            && Objects.equals(getPrice(), that.getPrice());
    }
}
