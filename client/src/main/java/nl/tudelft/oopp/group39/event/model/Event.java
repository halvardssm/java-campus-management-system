package nl.tudelft.oopp.group39.event.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.Objects;

public class Event {
    private Long id;
    private String type;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    /**
     * Creates an event.
     */
    public Event() {
    }

    /**
     * Creates an event.
     *
     * @param type      type of the event
     * @param startDate start date of event
     * @param endDate   end date of event
     */
    public Event(
        String type,
        LocalDate startDate,
        LocalDate endDate
    ) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Gets the id of the event.
     *
     * @return the event id
     */
    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    /**
     * Gets the starting date of the event.
     *
     * @return the starting date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of the event.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(id, event.id)
            && Objects.equals(type, event.type)
            && Objects.equals(startDate, event.startDate)
            && Objects.equals(endDate, event.endDate);
    }
}
