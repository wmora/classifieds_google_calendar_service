package com.ml.services.google.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class MeliGoogleCalendarService {

    //Global instance of an HTTP transport
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    // Golbal instance of the Json factory
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static com.google.api.services.calendar.Calendar client;

    private String calendarId;

    private Credential credential;

    public MeliGoogleCalendarService(String calendarId) {
        this();
        this.calendarId = calendarId;
    }

    public MeliGoogleCalendarService() {
        try {
            credential = authorize();
            client = new com.google.api.services.calendar.Calendar.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
                    "Classifieds Appointments API").build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Authorizes the installed application to access user's protected data
     *
     * @return
     * @throws Exception
     */
    private Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, MeliGoogleCalendarService.class.getResourceAsStream("/client_secrets.json"));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter")) {
            System.out.println(
                    "Invalid Client ID and Secret from https://code.google.com/apis/console/?api=calendar");
        }
        // set up file credential store
        FileCredentialStore credentialStore = new FileCredentialStore(
                new File(System.getProperty("user.home"), ".credentials/calendar.json"), JSON_FACTORY);
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR)).setCredentialStore(credentialStore).build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * @param event
     * @return created event from Calendar
     */
    public Event addEvent(Event event) {
        return addEvent(calendarId, event);
    }

    /**
     * @param calendarId
     * @param event
     * @return created event from Calendar
     */
    public Event addEvent(String calendarId, Event event) {
        try {
            Event result = client.events().insert(calendarId, event).execute();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param eventId
     * @return true if event exists, false otherwise
     */
    public boolean eventExists(String eventId) {
        return eventExists(calendarId, eventId);
    }

    /**
     * @param calendarId
     * @param eventId
     * @return true if event exists, false otherwise
     */
    public boolean eventExists(String calendarId, String eventId) {
        try {
            Event event = client.events().get(calendarId, eventId).execute();
            return !event.getId().isEmpty();
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param calendarId
     * @return true if calendar exists, false otherwise
     */
    public boolean calendarExists(String calendarId) {
        try {
            Calendar calendar = client.calendars().get(calendarId).execute();
            return !calendar.getId().isEmpty();
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param eventId ID of event to be deleted
     */
    public void deleteEvent(String eventId) {
        deleteEvent(calendarId, eventId);
    }

    /**
     * @param calendarId
     * @param eventId    ID of event to be deleted
     */
    public void deleteEvent(String calendarId, String eventId) {
        try {
            client.events().delete(calendarId, eventId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarId() {
        return calendarId;
    }
}
