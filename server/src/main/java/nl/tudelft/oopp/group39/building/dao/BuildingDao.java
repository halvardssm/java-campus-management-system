package nl.tudelft.oopp.group39.building.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
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


        Predicate pall = null;

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

                /* TODO
                case "reservables" : {

                    List<String> rvals = Arrays.asList((filters.get(key)).split(","));

                    for (String id : rvals) {

                    }

                    List<Reservable> reservables = new ArrayList<>();

                    p = cb.equal(building.get(key), filters.get(key));
                    break;
                }
                */

                case "name":
                case "location":
                case "description":
                default:
                    p = cb.like(building.get(key), "%" + filters.get(key) + "%");
                    break;
            }

            pall = pall == null ? p : cb.and(p, pall);

            rcq = rcq.where(pall);

        }

        TypedQuery<Building> query = em.createQuery(rcq);
        return query.getResultList();
    }
}
