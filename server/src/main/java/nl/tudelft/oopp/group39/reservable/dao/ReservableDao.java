package nl.tudelft.oopp.group39.reservable.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.building.entities.Building;
import org.springframework.stereotype.Component;

@Component
public class ReservableDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * Filters Reservables.
     *
     * @param params all params
     * @return filtered list
     */
    public <T> List<T> listReservables(Map<String, String> params, Class<T> resultClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<T> q = cb.createQuery(resultClass);
        Root<T> c = q.from(resultClass);
        q.select(c);
        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey(Building.MAPPED_NAME)) {
            predicates.add(cb.equal(
                c.get(Building.MAPPED_NAME).get(Building.COL_ID),
                params.get(Building.MAPPED_NAME)
            ));
        }

        q.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<T> query = em.createQuery(q);
        return query.getResultList();
    }
}