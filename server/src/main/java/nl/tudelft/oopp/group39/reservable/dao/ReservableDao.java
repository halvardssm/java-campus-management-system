package nl.tudelft.oopp.group39.reservable.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDao;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import org.springframework.stereotype.Component;

@Component
public class ReservableDao<T extends Reservable> extends AbstractDao<T> {
    @PersistenceContext
    private EntityManager em;

    /**
     * Filters Reservables.
     *
     * @param newParams all params
     * @return filtered list
     */
    public List<T> listReservables(Map<String, String> newParams, Class<T> clazz) {
        init(em, clazz, newParams);

        checkParam(Reservable.COL_BUILDING, (c, p) -> predicateEqualForeign(c, Building.COL_ID, p));

        return result();
    }
}