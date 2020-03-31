package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(value = { "timeOfDelivery", "reservationAmounts" })
public class Reservation {

    private Long id;
    private String timeOfPickup;
    private Long roomId;
    private String userId;

    @JsonProperty("room")
    public void setRoom(ObjectNode room) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Room nRoom = mapper.reader().forType(Room.class).readValue(room);
        this.roomId = nRoom.getId();
    }

    @JsonProperty("user")
    public void setUser(ObjectNode user) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User nUser = mapper.reader().forType(User.class).readValue(user);
        this.userId = nUser.getUsername();
    }

    public Reservation() {
    }

    /**
     * Constructor of Reservation.
     *
     * @param id                 the id
     * @param timeOfPickup       the time of the pickup
     */
    public Reservation(
        Long id,
        String timeOfPickup
    ) {
        this.id = id;
        this.timeOfPickup = timeOfPickup;
    }

    public long getId() { return id;}

    public String getTimeOfPickup() {
        return timeOfPickup;
    }

    public long getRoom() {
        return roomId;
    }

    public String getUser() {
        return userId;
    }

}
