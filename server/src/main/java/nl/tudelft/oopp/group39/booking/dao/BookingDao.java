package nl.tudelft.oopp.group39.booking.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class BookingDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * Filters Bookings.
     *
     * @param params all params
     * @return filtered list
     */
    public List<Booking> listBookings(Map<String, String> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Booking> q = cb.createQuery(Booking.class);
        Root<Booking> c = q.from(Booking.class);
        q.select(c);

        if (params.containsKey(Booking.COL_USER)) {
            q.where(cb.equal(
                c.get(Booking.COL_USER).get(User.COL_USERNAME),
                params.get(Booking.COL_USER)
            ));
        }

        if (params.containsKey(Booking.COL_ROOM)) {
            q.where(cb.equal(
                c.get(Booking.COL_ROOM).get(Room.COL_ID),
                params.get(Booking.COL_ROOM)
            ));
        }

        if (params.containsKey(Booking.COL_DATE)) {
            q.where(cb.equal(
                c.get(Booking.COL_DATE),
                LocalDate.parse(params.get(Booking.COL_DATE))
            ));
        }

        TypedQuery<Booking> query = em.createQuery(q);
        return query.getResultList();
    }
}
