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
import nl.tudelft.oopp.group39.config.AbstractEntity;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

@Entity
@Table(name = Reservable.TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(allowSetters = true, value = {Reservable.COL_RESERVATIONS})
@JsonIdentityInfo(
    generator = ObjectIdGenerators.None.class
)
public class Reservable extends AbstractEntity {
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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Reservable() {
    }

    /**
     * The constructor of Reservable.
     *
     * @param price        the price of the item
     * @param building     the building connected
     * @param reservations the reservations
     */
    public Reservable(
        Double price,
        Building building,
        Set<ReservationAmount> reservations
    ) {
        setBuilding(building);
        setPrice(price);
        this.reservations.addAll(initSet(reservations));
    }

    public Set<ReservationAmount> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationAmount> reservations) {
        this.reservations = reservations;
    }

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
