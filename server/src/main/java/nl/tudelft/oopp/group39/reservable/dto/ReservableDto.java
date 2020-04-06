package nl.tudelft.oopp.group39.reservable.dto;

import static nl.tudelft.oopp.group39.config.Utils.idToEntity;
import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;

public class ReservableDto<E extends Reservable<E, D>, D extends ReservableDto<E, D>>
    extends AbstractDto<E, D> {
    private Double price;
    private Long building;
    private Set<ReservationAmountDto> reservations = new HashSet<>();

    /**
     * Creates a ReservableDto.
     */
    public ReservableDto() {
    }

    /**
     * Creates a ReservableDto object.
     *
     * @param id           the id
     * @param price        the price of the reservable
     * @param building     the building id of the reservable
     * @param reservations the reservations associated with the reservable
     */
    public ReservableDto(
        Long id,
        Double price,
        Long building,
        Set<ReservationAmountDto> reservations
    ) {
        setId(id);
        setBuilding(building);
        setPrice(price);
        getReservations().addAll(initSet(reservations));
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
     * Gets the building where the reservable is at.
     *
     * @return the building where the reservable is at
     */
    public Long getBuilding() {
        return building;
    }

    /**
     * Changes the building where the reservable is at.
     *
     * @param building the new building
     */
    public void setBuilding(Long building) {
        this.building = building;
    }

    /**
     * Gets the reservations of the reservable.
     *
     * @return the reservations of the reservable
     */
    public Set<ReservationAmountDto> getReservations() {
        return reservations;
    }

    /**
     * Changes the reservations of the reservable.
     *
     * @param reservations the new set of reservations
     */
    public void setReservations(Set<ReservationAmountDto> reservations) {
        this.reservations.clear();
        this.reservations.addAll(reservations);
    }

    /**
     * Changes the ReservableDto to a Reservable object.
     *
     * @return a Reservable object
     */
    @Override
    public E toEntity() {
        Set<ReservationAmount> reservationAmount = Utils.setDtoToEntity(reservations);

        return (E) new Reservable(
            null,
            price,
            idToEntity(building, Building.class),
            reservationAmount
        );
    }
}
