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
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    @Autowired
    private RoomRepository roomRepository;

    @PersistenceContext
    private EntityManager em;

    public List<Room> roomFilter(Map<String,Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> rcq = cb.createQuery(Room.class);

        Root<Room> room = rcq.from(Room.class);

        Set<String> keys = filters.keySet();

        for (String key : keys) {
            Predicate p;

            Object test = filters.get(key);

            switch (key) {
                case "capacity": {

                    Integer cap = Integer.parseInt((String) filters.get(key));
                    p = cb.greaterThanOrEqualTo(room.get(key), cap);
                    break;
                }
                case "buildingId": {

                    Long cap = Long.parseLong((String) filters.get(key));
                    p = cb.greaterThanOrEqualTo(room.get(key), cap);
                    break;
                }
                case "facilities":


                    List<Integer> facvals = new ArrayList<>();

                    for (String val : ((String) filters.get(key)).split(",")) {
                        facvals.add(Integer.parseInt(val));
                    }


                    continue;
                default:
                    p = cb.like(room.get(key), "%" + filters.get(key) + "%");
                    break;
            }

            rcq = rcq.where(p);
        }
        TypedQuery<Room> query = em.createQuery(rcq);
        return query.getResultList();
    }
}
