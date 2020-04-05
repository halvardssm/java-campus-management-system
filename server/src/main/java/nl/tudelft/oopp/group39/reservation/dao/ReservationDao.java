package nl.tudelft.oopp.group39.reservation.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDao;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationDao extends AbstractDao<Reservation> {
    public static final String PARAM_DATE = "date";
    public static final String PARAM_RESERVABLE = "reservable";

    @PersistenceContext
    private EntityManager em;

    /**
     * Filter for reservations.
     *
     * @param newParams filters to be used.
     * @return the filtered values.
     */
    public List<Reservation> reservationFilter(Map<String, String> newParams) {
        init(em, Reservation.class, newParams);

        checkParam(PARAM_DATE, (c, p) -> predicateDateEqual(
            p,
            Reservation.COL_TIME_OF_PICKUP,
            Reservation.COL_TIME_OF_DELIVERY
        ));

        checkParam(
            Reservation.COL_TIME_OF_DELIVERY,
            (c, p) -> predicateSmaller(c, LocalDateTime.parse(p))
        );

        checkParam(Reservation.COL_ROOM, this::predicateEqual);
        checkParam(
            Reservation.COL_USER,
            (c, p) -> predicateEqualForeign(c, User.COL_USERNAME, p)
        );

        checkParam(PARAM_RESERVABLE, (c, p) -> predicateInRelationManyMany(
            p,
            Reservation.MAPPED_NAME,
            Reservable.COL_ID,
            ReservationAmount.class
        ));

        return result();
    }
}
