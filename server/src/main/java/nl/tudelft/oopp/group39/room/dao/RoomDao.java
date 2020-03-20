package nl.tudelft.oopp.group39.room.dao;

import java.util.ArrayList;
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
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomDao {

    @PersistenceContext
    private EntityManager em;

    /** TODO @Cleanup.
     *
     * @param filters filters retrieved
     * @return List rooms
     */
    public List<Room> roomFilter(Map<String,String> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> rcq = cb.createQuery(Room.class);
        Root<Room> room = rcq.from(Room.class);

        Set<String> keys = filters.keySet();

        Predicate pall = cb.conjunction();

        for (String key : keys) {
            Predicate p;

            switch (key) {
                case "capacity": {
                    Integer cap = Integer.parseInt(filters.get(key));
                    p = cb.greaterThanOrEqualTo(room.get(key), cap);
                    break;
                }
                case "buildingId":
                case "id": {
                    Long id = Long.parseLong(filters.get(key));
                    p = cb.greaterThanOrEqualTo(room.get(key), id);
                    break;
                }
                case "onlyStaff": {
                    boolean staff = Boolean.parseBoolean(filters.get(key));
                    p = cb.equal(room.get(key), staff);
                    break;
                }

                case "facilities": {

                    List<Integer> fvals = new ArrayList<>();

                    for (String val: (filters.get(key)).split(",")) {
                        fvals.add(Integer.parseInt(val));
                    }

                    CriteriaQuery<Facility> facq = cb.createQuery(Facility.class);
                    Root<Facility> facility = facq.from(Facility.class);

                    facq.select(facility.get(Room.TABLE_NAME));
                    facq.where(facility.get(Facility.COL_ID).in(fvals));

                    TypedQuery<Facility> nestq = em.createQuery(facq);

                    List<Facility> test = nestq.getResultList();

                    p = room.in(test);
                    break;
                }

                case "bookings": {

                    p = cb.isNotNull(room.get(key));

                    break;
                }

                case "description":
                default:
                    p = cb.like(room.get(key), "%" + filters.get(key) + "%");
                    break;
            }

            pall = cb.and(p, pall);
        }
        rcq = rcq.where(pall);

        TypedQuery<Room> query = em.createQuery(rcq);
        return query.getResultList();
    }
}
