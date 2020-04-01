package nl.tudelft.oopp.group39.user.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import nl.tudelft.oopp.group39.booking.model.Booking;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class CalendarController extends AbstractSceneController {

    public void createCalendar() throws JsonProcessingException {
        CalendarView calendarView = new CalendarView();
        Calendar bookingsCalendar = new Calendar("My bookings");
        bookingsCalendar.setReadOnly(true);
        bookingsCalendar.setStyle(Calendar.Style.STYLE2);
        Calendar eventsCalendar = new Calendar("Events");
        eventsCalendar.setReadOnly(true);
        eventsCalendar.setStyle(Calendar.Style.STYLE5);
        CalendarSource calendarSource = new CalendarSource("My calendars");
        calendarSource.getCalendars().addAll(bookingsCalendar, eventsCalendar);
        calendarView.getCalendarSources().clear();
        calendarView.getCalendarSources().add(calendarSource);
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
            bookingsCalendar.addEntry(entry);
        }
        Set<Event> events = getEventList();
        for (Event event : events) {
            Interval interval = new Interval(event.getStartDate(), LocalTime.of(0, 0), event.getEndDate(), LocalTime.of(23, 59));
            Entry<String> entry = new Entry<>(event.getType(), interval);
            entry.setFullDay(true);
            eventsCalendar.addEntry(entry);
        }
        window.setCenter(calendarView);
    }

    public Booking[] getBookings() throws JsonProcessingException {
        String bookingsString = ServerCommunication.getBookings("user=" + user.getUsername());
        ArrayNode body = (ArrayNode) mapper.readTree(bookingsString).get("body");
        bookingsString = mapper.writeValueAsString(body);
        return mapper.readValue(bookingsString, Booking[].class);
    }


}
