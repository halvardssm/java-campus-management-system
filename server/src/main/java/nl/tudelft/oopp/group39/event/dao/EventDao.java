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
    public static final String PARAM_DATE = "date";

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

        checkParam(Event.COL_ID, this::predicateLongInList);

        checkParam(Event.COL_TITLE, this::predicateLike);

        checkParam(
            PARAM_DATE,
            (c, p) -> predicateDateEqual(p, Event.COL_STARTS_AT, Event.COL_ENDS_AT)
        );

        checkParam(Event.COL_STARTS_AT, (c, p) -> predicateGreater(c, Utils.parseDateTime(p)));

        checkParam(Event.COL_ENDS_AT, (c, p) -> predicateSmaller(c, Utils.parseDateTime(p)));

        checkParam(Event.COL_IS_GLOBAL, (c, p) -> predicateEqual(c, Boolean.parseBoolean(p)));

        checkParam(Event.COL_USER, (c, p) -> predicateEqual(c, userRepository.getOne(p)));

        checkParam(Event.COL_ROOMS, (c, p) -> predicateInRelation(Event.TABLE_NAME, Room.class, p));

        return result();
    }
}
