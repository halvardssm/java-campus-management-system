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

public class ReservableDto extends AbstractDto<Reservable, ReservableDto> {
    private Double price;
    private Long building;
    private Set<ReservationAmountDto> reservations = new HashSet<>();

    public ReservableDto() {
    }

    /**
     * Creates a Dto object.
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getBuilding() {
        return building;
    }

    public void setBuilding(Long building) {
        this.building = building;
    }

    public Set<ReservationAmountDto> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationAmountDto> reservations) {
        this.reservations.clear();
        this.reservations.addAll(reservations);
    }

    @Override
    public Reservable toEntity() {
        Set<ReservationAmount> reservationAmount = Utils.setDtoToEntity(reservations);

        return new Reservable(null, price, idToEntity(building, Building.class), reservationAmount);
    }
}
