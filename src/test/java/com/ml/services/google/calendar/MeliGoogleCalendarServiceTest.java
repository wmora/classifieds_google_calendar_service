package com.ml.services.google.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * User: wmora
 * Date: 05/12/12
 */
public class MeliGoogleCalendarServiceTest extends TestCase {

    private String calendarIdOk;
    private String calendarIdNotOk;
    private MeliGoogleCalendarService service;
    private String eventIdOk;
    private String eventIdNotOk;
    private ArrayList<Event> events = new ArrayList<Event>();


    public void setUp() throws Exception {
        service = new MeliGoogleCalendarService();
        calendarIdOk = "mercadolibre.com_forlfsjld6nnk0je6a1gesg5vk@group.calendar.google.com";
        calendarIdNotOk = "this_calendar_does_not_exist";
        eventIdOk = getTestEventId();
        eventIdNotOk = "this_is_an_invalid_event_id";
    }

    private String getTestEventId() {
        Event event = new Event();
        List<EventAttendee> eventAttendees = new ArrayList<EventAttendee>();
        EventAttendee attendee1 = new EventAttendee();
        attendee1.setEmail("william.mora@mercadolibre.com.ve");
        eventAttendees.add(attendee1);
        event.setAttendees(eventAttendees);
        event.setSummary("Evento creado automáticamente");
        event.setDescription("Evento creado automáticamente con APIs de Google");
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 3600000);
        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
        event.setEnd(new EventDateTime().setDateTime(end));
        Event result = service.addEvent(calendarIdOk, event);
        events.add(result);
        return result.getId();
    }

    public void tearDown() throws Exception {
        for(Event e: events) {
            service.deleteEvent(calendarIdOk, e.getId());
            System.out.println(String.format("Event %s deleted", e.getId()));
        }
    }

    public void testAddEvent() throws Exception {
        String description = "Evento creado automáticamente con APIs de Google";
        String summary =  "Evento creado automáticamente";
        Event event = new Event();
        List<EventAttendee> eventAttendees = new ArrayList<EventAttendee>();
        EventAttendee attendee1 = new EventAttendee();
        attendee1.setEmail("william.mora@mercadolibre.com.ve");
        eventAttendees.add(attendee1);
        EventAttendee attendee2 = new EventAttendee();
        attendee2.setEmail("migcenel.gonzalez@mercadolibre.com.ve");
        eventAttendees.add(attendee2);
        EventAttendee attendee3 = new EventAttendee();
        attendee3.setEmail("franco.martinez@mercadolibre.com.ve");
        eventAttendees.add(attendee3);
        EventAttendee attendee4 = new EventAttendee();
        attendee4.setEmail("daniel.loreto@mercadolibre.com.ve");
        eventAttendees.add(attendee4);
        event.setAttendees(eventAttendees);
        event.setSummary(summary);
        event.setDescription(description);
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 3600000);
        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
        event.setEnd(new EventDateTime().setDateTime(end));
        Event result = service.addEvent(calendarIdOk, event);
        events.add(result);
        assertTrue(result.getDescription().equals(description));
        assertTrue(result.getSummary().equals(summary));
    }

    public void testEventExistsTrue() throws Exception {
        boolean result = service.eventExists(calendarIdOk, eventIdOk);
        assertTrue(result);
    }

    public void testEventExistsFalse() throws Exception {
        boolean result = service.eventExists(calendarIdOk, eventIdNotOk);
        assertFalse(result);
    }

    public void testCalendarExistsTrue() throws Exception {
        assertTrue(service.calendarExists(calendarIdOk));
    }

    public void testCalendarExistsFalse() throws Exception {
        assertFalse(service.calendarExists(calendarIdNotOk));
    }
}
