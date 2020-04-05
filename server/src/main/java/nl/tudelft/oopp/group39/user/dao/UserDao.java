package nl.tudelft.oopp.group39.user.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDao;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserDao extends AbstractDao<User> {
    @PersistenceContext
    private EntityManager em;

    /**
     * Filter for buildings.
     *
     * @param newParams filters to be used
     * @return the filtered values
     */
    public List<User> filter(Map<String, String> newParams) throws IllegalArgumentException {
        init(em, User.class, newParams);

        checkParam(User.COL_USERNAME, this::predicateLike);

        checkParam(User.COL_ROLE, (c, p) -> {
            Role role = Role.valueOf(p);

            predicateEqual(c, role);
        });

        return result();
    }
}
