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
import java.util.Set;
import nl.tudelft.oopp.group39.booking.model.Booking;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.reservable.model.Bike;
import nl.tudelft.oopp.group39.reservation.model.Reservation;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class CalendarController extends AbstractSceneController {
    /**
     * Creates calendarView of events, bookings and food/bike orders and shows it.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public void createCalendar() throws JsonProcessingException {
        CalendarView calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        Calendar bookingsCalendar = new Calendar("My bookings");
        bookingsCalendar.setReadOnly(true);
        bookingsCalendar.clear();
        bookingsCalendar.setStyle(Calendar.Style.STYLE2);
        Calendar eventsCalendar = new Calendar("Public Events");
        eventsCalendar.setReadOnly(true);
        eventsCalendar.clear();
        eventsCalendar.setStyle(Calendar.Style.STYLE5);
        Calendar myEventsCalendar = new Calendar("My Events");
        myEventsCalendar.setReadOnly(false);
        myEventsCalendar.clear();
        Calendar myReservables = new Calendar("My Food and Bike Orders");
        myReservables.setStyle(Calendar.Style.STYLE3);
        myReservables.setReadOnly(true);
        myReservables.clear();
        CalendarSource calendarSource = new CalendarSource("My calendars");
        calendarSource.getCalendars()
            .addAll(bookingsCalendar, eventsCalendar, myEventsCalendar, myReservables);
        calendarView.getCalendarSources().clear();
        calendarView.getCalendarSources().add(calendarSource);
        addBookings(bookingsCalendar);
        addPublicEvents(eventsCalendar);
        addPersonalEvents(myEventsCalendar);
        addReservables(myReservables);
        myEventsCalendar.addEventHandler(event -> {
            try {
                handleEvent(event);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        window.setCenter(calendarView);
    }

    /**
     * Adds bookings to the bookings calendar.
     *
     * @param calendar the bookings calendar to which the bookings need to be added.
     * @throws JsonProcessingException when there is a processing exception
     */
    public void addBookings(Calendar calendar) throws JsonProcessingException {
        Booking[] bookings = getBookings("user=" + user.getUsername());
        for (Booking booking : bookings) {
            Interval interval =
                new Interval(
                    LocalDate.parse(booking.getDate()),
                    LocalTime.parse(booking.getStartTime()),
                    LocalDate.parse(booking.getDate()),
                    LocalTime.parse(booking.getEndTime())
                );
            Entry<String> entry = new Entry<>(booking.getRoomObj().getName(), interval);
            entry.setLocation(booking.getRoomObj().getBuildingObject().getLocation());
            calendar.addEntry(entry);
        }
    }

    /**
     * Adds events to the public events calendar.
     *
     * @param calendar the events calendar to which the events need to be added
     * @throws JsonProcessingException when there is a processing exception
     */
    public void addPublicEvents(Calendar calendar) throws JsonProcessingException {
        Set<Event> events = getEvents("isGlobal=true");
        addEvents(calendar, events);
    }

    /**
     * Adds personal events to the personal events calender.
     *
     * @param calendar the personal events calendar to which the events need to be added
     * @throws JsonProcessingException when there is a processing exception
     */
    public void addPersonalEvents(Calendar calendar) throws JsonProcessingException {
        Set<Event> events = getEvents("user=" + user.getUsername() + "&isGlobal=false");
        addEvents(calendar, events);
    }

    /**
     * Adds given set of events to given calendar.
     *
     * @param calendar the calendar the events need to be added to
     * @param events   set of events that need to be added
     */
    public void addEvents(Calendar calendar, Set<Event> events) {
        for (Event event : events) {
            Interval interval = new Interval(event.getStartTime(), event.getEndTime());
            Entry<String> entry = new Entry<>(event.getTitle(), interval);
            if (event.isFullDay()) {
                entry.setFullDay(true);
            }
            calendar.addEntry(entry);
        }
    }

    /**
     * Adds the food and bike orders to the reservables calendar.
     *
     * @param calendar the reservables calendar to which the orders need to be added
     * @throws JsonProcessingException when there is a processing exception
     */
    public void addReservables(Calendar calendar) throws JsonProcessingException {
        String reservationString = ServerCommunication.getReservation("user=" + user.getUsername());
        System.out.println(reservationString);
        ArrayNode body = (ArrayNode) mapper.readTree(reservationString).get("body");
        reservationString = mapper.writeValueAsString(body);
        Reservation[] reservations = mapper.readValue(reservationString, Reservation[].class);
        for (Reservation reservation : reservations) {
            if (reservation.getTimeOfDelivery() != null && reservation.getRoom() == null) {
                Bike bike = ServerCommunication.getBike(reservation.getReservable());
                String location = bike.getBuildingObj().getName();
                Interval interval =
                    new Interval(reservation.getPickupTime(), reservation.getDeliveryTime());
                Entry<String> entry = new Entry<>(bike.getBikeType() + " Bike", interval);
                entry.setLocation(location);
                calendar.addEntry(entry);
            } else if (reservation.getRoom() != null) {
                Interval interval =
                    new Interval(reservation.getPickupTime(), reservation.getPickupTime());
                Room room = ServerCommunication.getRoom(reservation.getRoom());
                String location = room.getBuildingObject().getName() + " " + room.getName();
                Entry<String> entry = new Entry<>("Food order", interval);
                entry.setLocation(location);
                calendar.addEntry(entry);
            }
        }
    }

    /**
     * Handles a CalendarEvent.
     *
     * @param e CalendarEvent that happened
     * @throws JsonProcessingException when there is a processing exception
     */
    public void handleEvent(CalendarEvent e) throws JsonProcessingException {
        if (e.isEntryAdded()) {
            addEntry(e.getEntry());
        } else if (e.isEntryRemoved()) {
            removeEntry(e.getEntry());
        } else if (e.getEventType().getSuperType().equals(CalendarEvent.ENTRY_CHANGED)) {
            changeEntry(e);
        }
    }

    /**
     * Changes an event in the database.
     *
     * @param event the event that was fired.
     * @throws JsonProcessingException when there is a processing exception
     */
    public void changeEntry(CalendarEvent event) throws JsonProcessingException {
        Entry<?> entry = event.getEntry();
        System.out.println(entry);
        String title = event.getOldText() == null ? entry.getTitle() : event.getOldText();
        LocalDateTime startsAt;
        LocalDateTime endsAt;
        System.out.println(event.getOldFullDay());
        if (event.getOldFullDay()) {
            startsAt = LocalDateTime.of(
                event.getOldInterval().getStartDate(),
                LocalTime.of(0, 0, 0));
            endsAt = LocalDateTime.of(
                event.getOldInterval().getEndDate(),
                LocalTime.of(23, 59, 59));
        } else {
            startsAt =
                event.getOldInterval() == null ? entry.getStartAsLocalDateTime()
                    : event.getOldInterval().getStartDateTime();
            endsAt =
                event.getOldInterval() == null ? entry.getEndAsLocalDateTime()
                    : event.getOldInterval().getEndDateTime();
        }
        String filters = "title=" + title.replace(" ", "%20")
            + "&startsAt=" + startsAt
            + "&endsAt=" + endsAt
            + "&user=" + user.getUsername()
            + "&isGlobal=false";
        System.out.println(filters);
        Event oldEvent = ServerCommunication.getEvents(filters)[0];
        String start;
        String end;
        if (entry.isFullDay()) {
            start = entry.getStartDate().format(dateFormatter) + " 00:00:00";
            end = entry.getEndDate().format(dateFormatter) + " 23:59:59";
        } else {
            start = entry.getStartAsLocalDateTime().format(dateTimeFormatter);
            end = entry.getEndAsLocalDateTime().format(dateTimeFormatter);
        }
        Event newEvent = new Event(
            entry.getTitle(),
            start,
            end,
            false,
            user.getUsername(),
            null);
        System.out.println(ServerCommunication.updateEvent(newEvent, oldEvent.getId()));
    }

    /**
     * Adds an event to the database.
     *
     * @param entry the entry to be added
     * @throws JsonProcessingException when there is a processing exception
     */
    public void addEntry(Entry<?> entry) throws JsonProcessingException {
        Event event = new Event(
            entry.getTitle(),
            entry.getStartAsLocalDateTime().format(dateTimeFormatter),
            entry.getEndAsLocalDateTime().format(dateTimeFormatter),
            false,
            user.getUsername(),
            null);
        ServerCommunication.addEvent(event);
    }

    /**
     * Removes a personal event from database.
     *
     * @param entry that needs to be removed
     * @throws JsonProcessingException when there is a processing exception
     */
    public void removeEntry(Entry<?> entry) throws JsonProcessingException {
        System.out.println(entry);
        String title = entry.getTitle();
        LocalDateTime startsAt = entry.getStartAsLocalDateTime();
        LocalDateTime endsAt = entry.getEndAsLocalDateTime();
        String filters = "title=" + title.replace(" ", "%20")
            + "&startsAt=" + startsAt
            + "&endsAt=" + endsAt
            + "&user=" + user.getUsername()
            + "&isGlobal=false";
        Event oldEvent = ServerCommunication.getEvents(filters)[0];
        ServerCommunication.removeEvent(oldEvent.getId().toString());
    }
}
