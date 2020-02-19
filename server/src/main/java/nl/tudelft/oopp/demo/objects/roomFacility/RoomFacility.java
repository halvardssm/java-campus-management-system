package nl.tudelft.oopp.demo.objects.roomFacility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "roomfacilities")
public class RoomFacility {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "roomId")
    private int roomId;

    @Column(name = "facilityId")
    private int facilityId;

    public RoomFacility() {}

    public RoomFacility(int id, int roomId, int facilityId) {
        this.id = id;
        this.roomId = roomId;
        this.facilityId = facilityId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getFacilityId() {
        return facilityId;
    }
}