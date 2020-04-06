package nl.tudelft.oopp.group39.building.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.tudelft.oopp.group39.building.entities.Building;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDao;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import org.springframework.stereotype.Component;

@Component
public class BuildingDao extends AbstractDao<Building> {
    @PersistenceContext
    private EntityManager em;

    /**
     * Filter for buildings.
     *
     * @param newParams filters to be used
     * @return the filtered values
     */
    public List<Building> buildingFilter(Map<String, String> newParams) {
        init(em, Building.class, newParams);

        checkParam(Building.COL_OPEN, (c, p) -> predicateSmaller(c, LocalTime.parse(p)));

        checkParam(Building.COL_CLOSED, (c, p) -> predicateGreater(c, LocalTime.parse(p)));

        checkParam(
            Building.COL_RESERVABLES,
            (c, p) -> predicateInRelationManyOne(p, Building.TABLE_NAME, Reservable.class)
        );

        checkParam(Building.COL_NAME, this::predicateLike);

        checkParam(Building.COL_DESC, this::predicateLike);

        checkParam(Building.COL_LOCATION, this::predicateLike);

        return result();
    }
}
