package nl.tudelft.oopp.group39.room.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.building.repositories.BuildingRepository;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDao;
import nl.tudelft.oopp.group39.facility.entities.Facility;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomDao extends AbstractDao<Room> {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BuildingRepository buildingRepository;

    /**
     * Filter for the rooms.
     * It currently supports the values that are stored inside the Entity Room.
     *
     * @param newParams filters to filter the room with.
     *                  If entered an empty map, the program returns everything.
     * @return the list of the filtered, or all rooms
     */
    public List<Room> roomFilter(Map<String, String> newParams) {
        init(em, Room.class, newParams);

        checkParam(Room.COL_CAPACITY, (c, p) -> predicateGreater(c, Integer.parseInt(p)));

        checkParam(Room.COL_ID, this::predicateLongInList);

        checkParam(Room.COL_ONLY_STAFF, (c, p) -> predicateEqual(c, Boolean.parseBoolean(p)));

        checkParam(Room.COL_NAME, this::predicateLike);

        checkParam(Room.COL_BUILDING, this::predicateLongInList);

        checkParam(Room.COL_FACILITIES, (c, p) -> predicateInRelationManyOne(
            p,
            Room.TABLE_NAME,
            Facility.class
        ));

        checkParam(Room.COL_BOOKINGS, (c, p) -> predicateInRelationManyOne(
            p,
            Room.MAPPED_NAME,
            Booking.class
        ));

        return result();
    }
}
