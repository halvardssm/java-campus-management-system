package nl.tudelft.oopp.group39.building.dao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import org.springframework.stereotype.Component;

@Component
public class BuildingDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * TODO @Cleanup.
     *
     * @param filters filters retrieved
     * @return List rooms
     */
    public List<Building> buildingFilter(Map<String, String> filters) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Building> rcq = cb.createQuery(Building.class);
        Root<Building> building = rcq.from(Building.class);
        Set<String> keys = filters.keySet();

        List<Predicate> allPredicates = new ArrayList<>();

        if (keys.contains(Building.COL_OPEN)) {
            allPredicates.add(cb.lessThanOrEqualTo(
                building.get(Building.COL_OPEN),
                LocalTime.parse(filters.get(Building.COL_OPEN))
            ));
        }

        if (keys.contains(Building.COL_CLOSED)) {
            allPredicates.add(cb.greaterThanOrEqualTo(
                building.get(Building.COL_CLOSED),
                LocalTime.parse(filters.get(Building.COL_CLOSED))
            ));
        }

        if (keys.contains(Reservable.MAPPED_NAME)) {
            CriteriaQuery<Reservable> recq = cb.createQuery(Reservable.class);
            Root<Reservable> reservable = recq.from(Reservable.class);

            List<Integer> rvals = new ArrayList<>((Arrays.stream(
                filters.get(Reservable.MAPPED_NAME).split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList())
            ));

            recq.select(reservable.get(Building.MAPPED_NAME))
                .where(reservable.get(Reservable.COL_ID).in(rvals));

            allPredicates.add(building.in(em.createQuery(recq).getResultList()));
        }

        if (keys.contains(Building.COL_NAME)) {
            allPredicates.add(cb.like(building.get(Building.COL_NAME),
                "%" + filters.get(Building.COL_NAME) + "%"));
        }

        if (keys.contains(Building.COL_DESC)) {
            allPredicates.add(cb.like(building.get(Building.COL_DESC),
                "%" + filters.get(Building.COL_DESC) + "%"));
        }

        if (keys.contains(Building.COL_LOCATION)) {
            allPredicates.add(cb.like(building.get(Building.COL_LOCATION),
                "%" + filters.get(Building.COL_LOCATION) + "%"));
        }

        rcq.where(cb.and(allPredicates.toArray(new Predicate[0])));
        return em.createQuery(rcq).getResultList();
    }

}
