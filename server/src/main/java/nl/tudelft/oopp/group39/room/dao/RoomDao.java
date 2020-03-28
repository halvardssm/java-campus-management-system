package nl.tudelft.oopp.group39.room.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.building.repositories.BuildingRepository;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BuildingRepository buildingRepository;

    /**
     * Filter for the rooms.
     * It currently supports the values that are stored inside the Entity Room.
     *
     * @param filters filters to filter the room with.
     *                If entered an empty map, the program returns everything.
     *
     * @return the list of the filtered, or all rooms
     */
    public List<Room> roomFilter(Map<String, String> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> rcq = cb.createQuery(Room.class);
        Root<Room> room = rcq.from(Room.class);

        Set<String> keys = filters.keySet();

        List<Predicate> allPredicates = new ArrayList<>();

        if (keys.contains(Room.COL_CAPACITY)) {
            allPredicates.add(cb.greaterThanOrEqualTo(
                room.get(Room.COL_CAPACITY),
                Integer.parseInt(filters.get(Room.COL_CAPACITY))));
        }

        if (keys.contains(Room.COL_ID)) {
            allPredicates.add(cb.greaterThanOrEqualTo(
                room.get(Room.COL_ID),
                Long.parseLong(filters.get(Room.COL_ID))));
        }

        if (keys.contains(Room.COL_ONLY_STAFF)) {
            allPredicates.add(cb.equal(
                room.get(Room.COL_ONLY_STAFF),
                Boolean.parseBoolean(filters.get(Room.COL_ONLY_STAFF))));
        }

        if (keys.contains(Room.COL_NAME)) {
            allPredicates.add(cb.like(
                room.get(Room.COL_NAME),
                "%" + filters.get(Room.COL_NAME) + "%"
            ));
        }

        if (keys.contains(Building.MAPPED_NAME)) {
            allPredicates.add(cb.equal(
                room.get(Building.MAPPED_NAME),
                buildingRepository.findById(
                    Long.parseLong(filters.get(Building.MAPPED_NAME)))
            ));
        }

        if (keys.contains(Facility.TABLE_NAME)) {
            List<Integer> fvals = new ArrayList<>((Arrays.stream(
                filters.get(
                    Facility.TABLE_NAME)
                    .split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList())
            ));


            CriteriaQuery<Facility> facq = cb.createQuery(Facility.class);
            Root<Facility> facility = facq.from(Facility.class);

            facq.select(facility.get(Room.TABLE_NAME))
                .where(facility.get(Facility.COL_ID).in(fvals));

            allPredicates.add(facility.in(em.createQuery(facq).getResultList()));
        }

        if (keys.contains(Booking.TABLE_NAME)) {
            CriteriaQuery<Booking> bocq = cb.createQuery(Booking.class);
            Root<Booking> booking = bocq.from(Booking.class);

            List<Integer> bvals = new ArrayList<>((Arrays.stream(
                filters.get(
                    Booking.TABLE_NAME)
                    .split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList()))
            );

            bocq.select(booking.get(Room.MAPPED_NAME))
                .where(booking.get(Booking.COL_ID).in(bvals));

            allPredicates.add(room.in(em.createQuery(bocq).getResultList()));
        }

        rcq.where(cb.and(allPredicates.toArray(new Predicate[0])));
        TypedQuery<Room> query = em.createQuery(rcq);
        return query.getResultList();
    }
}
