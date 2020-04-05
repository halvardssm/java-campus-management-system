package nl.tudelft.oopp.group39.booking.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDao;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class BookingDao extends AbstractDao<Booking> {
    @PersistenceContext
    private EntityManager em;

    /**
     * Filters Bookings.
     *
     * @param newParams all params
     * @return filtered list
     */
    public List<Booking> listBookings(Map<String, String> newParams) {
        init(em, Booking.class, newParams);

        checkParam(Booking.COL_USER, (c, p) -> predicateEqualForeign(c, User.COL_USERNAME, p));

        checkParam(Booking.COL_ROOM, (c, p) -> predicateEqualForeign(c, Room.COL_ID, p));

        checkParam(Booking.COL_DATE, (c, p) -> predicateEqual(c, LocalDate.parse(p)));

        return result();
    }
}
