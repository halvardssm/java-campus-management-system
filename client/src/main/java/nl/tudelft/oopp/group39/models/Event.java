package nl.tudelft.oopp.group39.models;

public class Event {

    private int id;
    private String type;
    private String startDate;
    private String endDate;

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
        String startDate,
        String endDate
    ) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

}
