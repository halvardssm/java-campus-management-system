package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class Room {
    private long id;
    private int capacity;
    private String name;
    private boolean onlyStaff;
    private String description;
    private ArrayNode facilities;
    //    private Set<Event> events = new HashSet<>();
//    private Set<Booking> bookings = new HashSet<>();
    private long building;

    public Room() {

    }

    public Room(int capacity, String name, boolean onlyStaff, String description, long buildingId, ArrayNode facilities) {
        this.building = buildingId;
        this.name = name;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities = facilities;
//        this.facilities.addAll(facilities != null ? facilities : new HashSet<>());
//        this.events.addAll(events != null ? events : new HashSet<>());
//        this.bookings.addAll(bookings != null ? bookings : new HashSet<>());
    }

    public long getId() {
        return id;
    }

    public long getBuildingId() {
        return building;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOnlyStaff() {
        return onlyStaff;
    }

    public String getDescription() {
        return description;
    }

    public ArrayNode getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayNode facilities) {
        this.facilities = facilities;
    }

    public String facilitiesToString() {
        if (facilities.size() == 0) {
            return "none";
        } else {
            String result = "";
            for (int i = 0; i < facilities.size(); i++) {
                if (i == facilities.size() - 1) {
                    result += facilities.get(i).get("description").asText();
                } else {
                    result += facilities.get(i).get("description").asText() + ", ";
                }
            }
            return result;
        }
    }
//
//    public Set<Booking> getBookings() {
//        return bookings;
//    }

}
