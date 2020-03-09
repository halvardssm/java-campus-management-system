package nl.tudelft.oopp.group39.event.entities;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
import nl.tudelft.oopp.group39.room.entities.Room;

@Entity
@Table(name = Event.TABLE_NAME)
public class Event {
    public static final String TABLE_NAME = "events";

    @Id
    private Integer id;
    @Enumerated(EnumType.STRING)
    private EventTypes type;

    @OneToMany(mappedBy = Room.TABLE_NAME)
    private Set<Room> rooms;

}
