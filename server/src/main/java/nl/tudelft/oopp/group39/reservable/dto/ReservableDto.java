package nl.tudelft.oopp.group39.reservable.dto;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import java.util.HashSet;
import java.util.Set;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;

public class ReservableDto {
    private Double price;
    private Integer building;
    private Set<ReservationAmountDto> reservations = new HashSet<>();

    public ReservableDto() {
    }

    public ReservableDto(
        Double price,
        Integer building,
        Set<ReservationAmountDto> reservations
    ) {
        this.price = price;
        this.building = building;
        this.reservations.addAll(reservations);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getBuilding() {
        return building;
    }

    public void setBuilding(Integer building) {
        this.building = building;
    }

    public Set<ReservationAmountDto> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationAmountDto> reservations) {
        this.reservations.clear();
        this.reservations.addAll(reservations);
    }
}
