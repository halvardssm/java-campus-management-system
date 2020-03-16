package nl.tudelft.oopp.group39.reservable.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservableDao {
    @Autowired
    private Session session;

    /**
     * Filters resevables.
     *
     * @param params request parameters
     * @return list of reservables
     */
    public <T> List<T> listReservables(Map<String, String> params, Class<T> resultClass) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(resultClass);

//        if(params.containsKey(Building.MAPPED_NAME)){
//            cb.and(cq.where(el->el))
//        }

        CriteriaQuery<T> all = cq.select(cq.from(resultClass));

        TypedQuery<T> allQuery = session.createQuery(all);
        return allQuery.getResultList();
    }
}
