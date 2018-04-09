import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.model.Calendar;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client extends WebSocketServer implements Runnable{
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    // Three Client Calendars
    com.google.api.services.calendar.model.Calendar socialCalendar;
    com.google.api.services.calendar.model.Calendar classCalendar;
    com.google.api.services.calendar.model.Calendar groupCalendar;

    // Three Calendar ID
    String socialCalendarId;
    String classCalendarId;
    String groupCalendarId;

    // Status Of Roommates
    String roommatesStatus;

    // Store the service;
    com.google.api.services.calendar.Calendar service;

    // Map from a string to its calendar ID
    HashMap<String, String> summaryToId;

    // groupId of a person
    String groupId;
    private static int TCP_PORT = 4444;

    private Set<WebSocket> conns;

    // Client Constructor
    public Client(String username, String ipAddress, int port) throws IOException {
        super(new InetSocketAddress(TCP_PORT));
        conns = new HashSet<>();
        this.start();

        // get oath credential
        service = getCalendarService();

        // Create the connection between client and server
        System.out.println("Trying to connect to " + port +  ":" + port);
        Socket s = new Socket(ipAddress, port);
        oos = new ObjectOutputStream(s.getOutputStream());
        ois = new ObjectInputStream(s.getInputStream());

        //new Client().start();
        Scanner scan = new Scanner(System.in);

        //TODO NEED TO CHECK WHETHER THIS IS A NEW LOGIN CLIENT OR THE CLIENT WHO HAS ALREADY SIGNED IN


        // Retrieve a specific calendar list entry
        summaryToId = new HashMap<>();
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();


            for (CalendarListEntry calendarListEntry : items) {
                String id = calendarListEntry.getId();
                String summary = calendarListEntry.getSummary();
                summaryToId.put(summary,id);
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);


        // choose calendar
        socialCalendarId = chooseCalendar(scan, summaryToId);
        socialCalendar= service.calendars().get(socialCalendarId).execute();
        classCalendarId =  chooseCalendar(scan, summaryToId);
        classCalendar = service.calendars().get(classCalendarId).execute();
        //TODO ADD TO DATABASE

        //TODO ASK WHETHER THEY ALREADY HAVE A CALENDAR WHICH CAN ACT LIKE A GROUP CALENDAR,
        //TODO IF YES THEN ENTER THE CHOOSE CALENDAR FUNCTION AGAIN, IF NO EXECUTE THE ADD
        //TODO EXECUTE THE ADD THE NEW CALENDAR FUNCTION
        //addCalendar();

        displayEvents(socialCalendarId);

        // TODO ADD EVENT FUNCTION

        /*
        String calendarId = "primary";
        Event event = addEvent();
        service.events().insert(calendarId, event).execute();
        System.out.printf("Event created! ");
        */
        // TODO ADD THE TOGGLE PART

        while(true) {
            try {
                Command returnItem = (Command) ois.readObject();
                if (returnItem.getCommandType().equals(CommandType.GROUP_EVENT)) {
                    Event newEvent = (Event) returnItem.getObj();
                    service.events().calendarImport(groupCalendarId, newEvent).execute();
                } else {
                    // TODO STATUS TOGGLE SHOULD I HAVE A LIST OF GROUP MEMBER STATUS?
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        /*
        System.out.println("Message");
        Command command = new Command(CommandType.GROUP_EVENT, message);
        this.sendObject(command);
        */
        String[] parts = message.split(",");
        for(String string:parts){
            System.out.println(string);
        }
        Command command;
        if(parts[0].equals("toggle")){
            System.out.println("The command is toggle event");
            command = new Command(CommandType.TOGGLE_EVNET, parts[1]);
            this.sendObject(command);
        }
        else {
            System.out.println("The command is add new event");
            //DateTime startingDateTime = new DateTime("2015-05-28T09:00:00-07:00");
            DateTime startingDateTime = new DateTime(parts[2]+":00-07:00");
            System.out.println("Creating a new starting time");
            DateTime endingDateTime = new DateTime(parts[3]+":00-07:00");
            System.out.println("Creating an ending time");
            Event event = addEvent(parts[1],startingDateTime, endingDateTime);
            String calendar = parts[4];
            System.out.println(calendar);
            if(calendar.equals("Group Calendar")){
                System.out.println("People are adding the event into group calendar");
                command = new Command(CommandType.GROUP_EVENT, event);
                this.sendObject(command);
            }
            else{
                if(calendar.equals("Class Calendar")){
                    System.out.println("People are adding the event into class calendar");
                    eventToCalendar(classCalendarId,event);
                }else{
                    System.out.println("People are adding the event into social calendar");
                    eventToCalendar(socialCalendarId,event);
                }
            }

        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            conns.remove(conn);
            // do some thing if required
        }
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    public void updatedStatus(String status){
        //TODO WRITE TO DATABSE & OR / UPDATED HERE?
        roommatesStatus = status;
        sendToggle(roommatesStatus);
    }

    // Toggle
    public void sendToggle(String status){
        Command command = new Command(CommandType.TOGGLE_EVNET, status);
        this.sendObject(command);
    }

    // Check String
    public String displayPrimary(String string){
        if(string.contains("@")){
            return "primary";
        }
        else{
            return string;
        }
    }

    public static Event addEvent(String eventTitle, DateTime startDateTime, DateTime endDateTime){
        Event event = new Event().setSummary(eventTitle);

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");

        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");

        event.setEnd(end);

        System.out.println("Adding event successfully");
        return event;
    }

    // Add Event To Calendar
    public void eventToCalendar(String calendarId, Event event)
    {
        try{
            this.service.events().insert(calendarId, event).execute();
            System.out.printf("Event created! ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Event builder
    public static Event addEvent(){
        // TODO CREATE AN ADD EVENT
        Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        // set the date and time of the event
        DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        // set recurrence of the event
        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        // set the attendees of the event
        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("lpage@example.com"),
                new EventAttendee().setEmail("sbrin@example.com"),
        };

        event.setAttendees(Arrays.asList(attendees));

        // set the reminder of the event
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
        return event;
    }


    // Choose the calendar you want to use as the personal/social calendar
    public String chooseCalendar(Scanner scan, HashMap<String, String> summaryToId ){
        while(true) {
            System.out.println("Please choose the calendar you want to choose as social" +
                    "class in the following: ");
            int number = 1;
            for(Map.Entry<String, String> entry: summaryToId.entrySet()){
                System.out.println(number + ") " + entry.getKey());
                number ++;
            }
            String keyWord = scan.next();
            if(summaryToId.get(keyWord) != null){
                return summaryToId.get(keyWord);
            }
            else
            {
                System.out.println("Your choice is invalid");
            }
        }
    }

    // Create a new calendar and add it into the current calendar list
    public void addCalendar()
    {
        try {
            com.google.api.services.calendar.model.Calendar calendar = new Calendar();
            calendar.setSummary("calendarSummary");
            calendar.setTimeZone("America/Los_Angeles");
            Calendar createdCalendar = service.calendars().insert(calendar).execute();
            System.out.println("I add a new calendar， which has a calendar id of " + createdCalendar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayEvents(String calendarId){
        try{
            // Retrieve the user account
            com.google.api.services.calendar.model.Calendar alreadyExistedCalendar =
                    service.calendars().get(calendarId).execute();

            System.out.println(alreadyExistedCalendar.getSummary());

            /* List the next 10 events from the primary calendar. */
            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = null;
            events = service.events().list(calendarId)
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();
            if (items.size() == 0) {
                System.out.println("No upcoming events found.");
            } else {
                System.out.println("Upcoming events");
                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    System.out.printf("%s (%s)\n", event.getSummary(), start);
                } }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // TODO ADD NOTIFICATION

    //NOTE ALL THE BELOW ARE FROM CALENDAR API FOR CREDENTIAL DON'T CHANGE THE CODE
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Calendar API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                Client.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp
                (flow, new LocalServerReceiver.Builder().setPort(8080).build()).
                authorize("user8");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static com.google.api.services.calendar.Calendar
    getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void sendObject(Command command) {
        try {
            if(oos == null)
            {
                System.out.println("oos is null");
            }
            oos.writeObject(command);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client player = new Client( "Username","localhost", 6789);
       // Client player = new Client( "Username","2001:0:9d38:90d7:ec:2503:bb4a:31ee", 6789);
    }
}