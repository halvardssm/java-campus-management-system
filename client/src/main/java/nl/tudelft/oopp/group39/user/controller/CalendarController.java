package nl.tudelft.oopp.group39.user.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.model.Booking;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class CalendarController extends AbstractSceneController {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void createCalendar() throws JsonProcessingException {
        CalendarView calendarView = new CalendarView();
        Calendar bookingsCalendar = new Calendar("My bookings");
        bookingsCalendar.setReadOnly(true);
        bookingsCalendar.clear();
        bookingsCalendar.setStyle(Calendar.Style.STYLE2);
        Calendar eventsCalendar = new Calendar("Public events");
        eventsCalendar.setReadOnly(true);
        eventsCalendar.clear();
        eventsCalendar.setStyle(Calendar.Style.STYLE5);
        Calendar myEventsCalendar = new Calendar("My events");
        myEventsCalendar.setReadOnly(false);
        myEventsCalendar.clear();

        CalendarSource calendarSource = new CalendarSource("My calendars");
        calendarSource.getCalendars().addAll(bookingsCalendar, eventsCalendar, myEventsCalendar);
        calendarView.getCalendarSources().clear();
        calendarView.getCalendarSources().add(calendarSource);
        addBookings(bookingsCalendar);
        addEvents(eventsCalendar);
        addPersonalEvents(myEventsCalendar);
        myEventsCalendar.addEventHandler(event -> {
            try {
                handleEvent(event);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        window.setCenter(calendarView);
    }

    public void addBookings(Calendar calendar) throws JsonProcessingException {
        Booking[] bookings = getBookings();
        for (Booking booking : bookings) {
            Interval interval =
                new Interval(
                    LocalDate.parse(booking.getDate()),
                    LocalTime.parse(booking.getStartTime()),
                    LocalDate.parse(booking.getDate()),
                    LocalTime.parse(booking.getEndTime())
                );
            Entry<String> entry = new Entry<>(booking.getRoomName(), interval);
            entry.setLocation(booking.getLocation());
            calendar.addEntry(entry);
        }
    }

    public void addEvents(Calendar calendar) throws JsonProcessingException {
        Set<Event> events = getEventList();
        for (Event event : events) {
            Interval interval = new Interval(event.getStartTime(), event.getEndTime());
            Entry<String> entry = new Entry<>(event.getTitle(), interval);
            entry.setFullDay(true);
            calendar.addEntry(entry);
        }
    }

    public void addPersonalEvents(Calendar calendar) throws JsonProcessingException {
        String eventsString = ServerCommunication.getEvents("user=" + user.getUsername() + "&isGlobal=false");
        ArrayNode body = (ArrayNode) mapper.readTree(eventsString).get("body");
        eventsString = mapper.writeValueAsString(body);
        Event[] events = mapper.readValue(eventsString, Event[].class);
        for (Event event : events) {
            Interval interval = new Interval(event.getStartTime(), event.getEndTime());
            Entry<String> entry = new Entry<>(event.getTitle(), interval);
            calendar.addEntry(entry);
        }
    }

    public Booking[] getBookings() throws JsonProcessingException {
        String bookingsString = ServerCommunication.getBookings("user=" + user.getUsername());
        ArrayNode body = (ArrayNode) mapper.readTree(bookingsString).get("body");
        bookingsString = mapper.writeValueAsString(body);
        return mapper.readValue(bookingsString, Booking[].class);
    }

    public void handleEvent(CalendarEvent e) throws JsonProcessingException {
        if (e.isEntryAdded()) {
            System.out.println(e.getEntry());
            addEntry(e.getEntry());
        } else if (e.isEntryRemoved()) {
            System.out.println("remove entry");
        } else if (e.getEventType().getSuperType().equals(CalendarEvent.ENTRY_CHANGED)) {
            changeEntry(e);
        }
    }

    public void changeEntry(CalendarEvent event) throws JsonProcessingException {
        Entry<?> entry = event.getEntry();
        String title = event.getOldText() == null ? entry.getTitle() : event.getOldText();
        LocalDateTime startsAt = event.getOldInterval() == null ? entry.getStartAsLocalDateTime() : event.getOldInterval().getStartDateTime();
        LocalDateTime endsAt = event.getOldInterval() == null ? entry.getEndAsLocalDateTime() : event.getOldInterval().getEndDateTime();
        String filters = "title=" + title.replace(" ", "%20")
            + "&startsAt=" + startsAt
            + "&endsAt=" + endsAt
            + "&user=" + user.getUsername()
            + "&isGlobal=false";
        System.out.println(filters);
        Event oldEvent = ServerCommunication.getEvent(filters)[0];
        Event newEvent = new Event(
            entry.getTitle(),
            entry.getStartAsLocalDateTime().format(formatter),
            entry.getEndAsLocalDateTime().format(formatter),
            false,
            user.getUsername(),
            null);
        System.out.println(ServerCommunication.updateEvent(newEvent, oldEvent.getId()));
    }

    public void addEntry(Entry<?> entry) throws JsonProcessingException {
        System.out.println(entry.getTitle() + entry.getStartAsLocalDateTime() + entry.getEndAsLocalDateTime());
        Event event = new Event(
            entry.getTitle(),
            entry.getStartAsLocalDateTime().format(formatter),
            entry.getEndAsLocalDateTime().format(formatter),
            false,
            user.getUsername(),
            null);
        System.out.println(ServerCommunication.addEvent(event));
    }

    public List<Long> getAllRooms() throws JsonProcessingException {
        String roomsString = ServerCommunication.get(ServerCommunication.room);
        ArrayNode body = (ArrayNode) mapper.readTree(roomsString).get("body");
        roomsString = mapper.writeValueAsString(body);
        Room[] rooms = mapper.readValue(roomsString, Room[].class);
        List<Long> allRooms = new ArrayList<>();
        for (Room room : rooms) {
            allRooms.add(room.getId());
        }
        return allRooms;
    }

}
