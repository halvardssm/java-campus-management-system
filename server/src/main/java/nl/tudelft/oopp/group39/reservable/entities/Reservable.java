package nl.tudelft.oopp.group39.reservable.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;

@Entity
@Table(name = Reservable.TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
public class Reservable {
    public static final String TABLE_NAME = "reservables";
    public static final String MAPPED_NAME = "reservable";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double price;
    @ManyToOne
    @JoinColumn(name = Building.MAPPED_NAME)
    private Building building;
    @ManyToOne
    @JoinColumn(name = Reservation.MAPPED_NAME)
    private Reservation reservation;

    public Reservable() {
    }

    /**
     * The constructor of Reservable.
     *
     * @param building    the building connected
     * @param price       the price of the item
     * @param reservation the reservation
     */
    public Reservable(Building building, Double price, Reservation reservation) {
        setBuilding(building);
        setPrice(price);
        setReservation(reservation);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
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
