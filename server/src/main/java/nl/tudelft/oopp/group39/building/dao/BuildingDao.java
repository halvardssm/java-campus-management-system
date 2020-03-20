package nl.tudelft.oopp.group39.building.dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import org.springframework.stereotype.Component;

@Component
public class BuildingDao {

    @PersistenceContext
    private EntityManager em;

    /** TODO @Cleanup.
     *
     * @param filters filters retrieved
     * @return List rooms
     */
    public List<Building> buildingFilter(Map<String,String> filters) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Building> rcq = cb.createQuery(Building.class);

        Root<Building> building = rcq.from(Building.class);
        Set<String> keys = filters.keySet();

        Predicate pall = cb.conjunction();

        for (String key : keys) {
            Predicate p;

            switch (key) {

                case "open":
                case "closed": {
                    String timeString = filters.get(key);

                    LocalTime time = LocalTime.parse(timeString);
                    p = cb.equal(building.get(key), time);

                    break;
                }

                /** TODO
                 *
                 */
                case "reservables" : {

                    List<Integer> rvals = new ArrayList<>();

                    for (String val: (filters.get(key)).split(",")) {
                        rvals.add(Integer.parseInt(val));
                    }

                    CriteriaQuery<Reservable> recq = cb.createQuery(Reservable.class);
                    Root<Reservable> reservable = recq.from(Reservable.class);

                    recq.select(reservable.get(Building.MAPPED_NAME));
                    recq.where(reservable.get(Reservable.COL_ID).in(rvals));

                    TypedQuery<Reservable> nestq = em.createQuery(recq);

                    p = building.in(nestq.getResultList());
                    break;
                }

                case "name":
                case "location":
                case "description":
                default:
                    p = cb.like(building.get(key), "%" + filters.get(key) + "%");
                    break;
            }

            pall = cb.and(p, pall);


        }
        rcq.where(pall);

        TypedQuery<Building> query = em.createQuery(rcq);
        return query.getResultList();
    }
}
