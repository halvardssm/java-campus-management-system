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
    private long id;

    @Column(name = "roomId")
    private long roomId;

    @Column(name = "facilityId")
    private long facilityId;

    public RoomFacility() {}

    public RoomFacility(long id, long roomId, long facilityId) {
        this.id = id;
        this.roomId = roomId;
        this.facilityId = facilityId;
    }

    public long getId() {
        return id;
    }
    public long getRoomId() {
        return roomId;
    }
    public long getFacilityId() {
        return facilityId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
    public void setFacilityId(long facilityId) {
        this.facilityId = facilityId;
    }
}