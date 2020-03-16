package nl.tudelft.oopp.group39.booking.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingDao {
    @PersistenceContext
    private EntityManager em;

    public List<Booking> listBookings(Map<String, String> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Booking> rcq = cb.createQuery(Booking.class);
        Root<Booking> room = rcq.from(Booking.class);
        Long cap = Long.parseLong(params.get("user"));
        Predicate p = cb.greaterThanOrEqualTo(room.get("user"), cap);
        rcq = rcq.where(p);

        TypedQuery<Booking> query = em.createQuery(rcq);
        return query.getResultList();
    }
}
