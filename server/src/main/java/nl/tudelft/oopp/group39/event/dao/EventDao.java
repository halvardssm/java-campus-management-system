package nl.tudelft.oopp.group39.event.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDao;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDao extends AbstractDao<Event> {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private UserRepository userRepository;

    /**
     * Filters Events.
     *
     * @param newParams all params
     * @return filtered list
     */
    public List<Event> filter(Map<String, String> newParams) {
        init(em, Event.class, newParams);

        checkParam(Event.COL_ID, (c) -> predicateLongInList(c, params.get(Event.COL_ID)));

        checkParam(Event.COL_TITLE, (c) -> predicateLike(c, params.get(Event.COL_TITLE)));

        checkParam(Event.COL_STARTS_AT, (c) -> predicateGreater(
            c,
            Utils.parseDateTime(params.get(Event.COL_STARTS_AT))
        ));

        checkParam(
            Event.COL_ENDS_AT,
            (c) -> predicateSmaller(c, Utils.parseDateTime(params.get(Event.COL_ENDS_AT)))
        );

        checkParam(
            Event.COL_IS_GLOBAL,
            (c) -> predicateEqual(c, Boolean.parseBoolean(params.get(Event.COL_IS_GLOBAL)))
        );

        checkParam(
            Event.COL_USER,
            (c) -> predicateEqual(c, userRepository.getOne(params.get(Event.COL_USER)))
        );

        checkParam(Event.COL_TITLE, (c) -> predicateInRelation(
            c,
            Room.class,
            params.get(Room.MAPPED_NAME)
            )
        );

        return result();
    }
}
