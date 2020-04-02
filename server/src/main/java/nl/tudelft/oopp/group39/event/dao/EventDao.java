package nl.tudelft.oopp.group39.event.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDao {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    /**
     * Filters Events.
     *
     * @param params all params
     * @return filtered list
     */
    public List<Event> list(Map<String, String> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Event> q = cb.createQuery(Event.class);
        Root<Event> c = q.from(Event.class);
        q.select(c);
        List<Predicate> predicates = new ArrayList<>();

        if (params.containsKey(Event.COL_ID)) {
            predicates.add(cb.greaterThanOrEqualTo(
                c.get(Event.COL_ID),
                Long.parseLong(params.get(Event.COL_ID))
            ));

            List<Long> idList = new ArrayList<>((
                Arrays.stream(params.get(Event.COL_ID).split(","))
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .collect(Collectors.toList())
            ));

            predicates.add(cb.in(c.get(Event.COL_ID).in(idList)));
        }

        if (params.containsKey(Event.COL_TITLE)) {
            predicates.add(cb.like(
                c.get(Event.COL_TITLE),
                "%" + params.get(Event.COL_TITLE) + "%"
            ));
        }

        if (params.containsKey(Event.COL_STARTS_AT)) {
            predicates.add(cb.greaterThanOrEqualTo(
                c.get(Event.COL_STARTS_AT),
                LocalDateTime.parse(params.get(Event.COL_STARTS_AT))
            ));
        }

        if (params.containsKey(Event.COL_ENDS_AT)) {
            predicates.add(cb.lessThanOrEqualTo(
                c.get(Event.COL_ENDS_AT),
                LocalDateTime.parse(params.get(Event.COL_ENDS_AT))
            ));
        }

        if (params.containsKey(Event.COL_ENDS_AT)) {
            predicates.add(cb.lessThanOrEqualTo(
                c.get(Event.COL_ENDS_AT),
                LocalDateTime.parse(params.get(Event.COL_ENDS_AT))
            ));
        }

        if (params.containsKey(Event.COL_IS_GLOBAL)) {
            predicates.add(cb.equal(
                c.get(Event.COL_IS_GLOBAL),
                Boolean.parseBoolean(params.get(Event.COL_IS_GLOBAL))
            ));
        }

        if (params.containsKey(Event.COL_IS_GLOBAL)) {
            predicates.add(cb.equal(
                c.get(Event.COL_IS_GLOBAL),
                Boolean.parseBoolean(params.get(Event.COL_IS_GLOBAL))
            ));
        }

        if (params.containsKey(Event.COL_USER)) {
            predicates.add(cb.equal(
                c.get(User.MAPPED_NAME),
                userRepository.getOne((params.get(Event.COL_USER)))
            ));
        }

        q.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Event> query = em.createQuery(q);
        return query.getResultList();
    }
}
