package nl.tudelft.oopp.group39.reservation.model;

public class ReservableNode {
    private Integer id;
    private Integer amount;
    private Integer reservable;

    /**
     * Creates a reservation.
     */
    public ReservableNode() {
    }

    /**
     * Creates a reservation.
     *
     * @param id                 the id of the reservation
     */
    public ReservableNode(
        Integer id,
        Integer amount,
        Integer reservable
    ) {
        this.id = id;
        this.amount = amount;
        this.reservable = reservable;
    }

    /**
     * Gets the id of the reservation.
     *
     * @return the id of the reservation
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the time of delivery for the reservation.
     *
     * @return the time of delivery for the reservation
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Gets the pick up time for the reservation.
     *
     * @return the pick up time for the reservation
     */
    public Integer getReservable() {
        return reservable;
    }


}
