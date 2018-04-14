package servlet;

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
import model.User;
import model.UserDAO;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Client extends WebSocketServer{

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    // Three servlet.Client Calendars
    com.google.api.services.calendar.model.Calendar socialCalendar;
    com.google.api.services.calendar.model.Calendar classCalendar;
    com.google.api.services.calendar.model.Calendar groupCalendar;

    // Group Id
    String groupId;

    // Three Calendar ID
    String socialCalendarIdOfCurrentUser;
    String classCalendarIdOfCurrentUser;
    String groupCalendarIdOfCurrentUser;

    // Status Of Roommates
    String roommatesStatus;

    // String of Username
    String userNameOfCurrentUser;

    // Store the service;
    com.google.api.services.calendar.Calendar service;

    // Map from a string to its calendar ID
    private HashMap<String, String> summaryToId;

    public HashMap<String, String> getSummaryToId() {
        return summaryToId;
    }

    // Current WebSocket
    WebSocket currentWebsocket;

    private static int TCP_PORT = 4444;

    private Set<WebSocket> conns;


    // servlet.Client Constructor
    public Client(String username, String ipAddress, int port) throws IOException {
        super(new InetSocketAddress(TCP_PORT));
        conns = new HashSet<>();
        this.start();

        // get oath credential
        service = getCalendarService();
        this.userNameOfCurrentUser = username;

        // Create the connection between client and server
        System.out.println("Trying to connect to " + port + ":" + port);
        //Socket s = new Socket(ipAddress, port);
       // oos = new ObjectOutputStream(s.getOutputStream());
        //ois = new ObjectInputStream(s.getInputStream());

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
                summaryToId.put(summary, id);
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);

        //listen();
    }

    private void listen(){
        while(true) {
            try {
                System.out.println("Inside of the client function the user name is " + userNameOfCurrentUser);
                System.out.println("Inside of the client function the group calendar id is " + groupCalendarIdOfCurrentUser);
                Command returnItem = (Command) ois.readObject();
                if (returnItem.getCommandType().equals(CommandType.GROUP_EVENT)) {
                    System.out.println("We are now adding a new event according to the server");
                    ArrayList<String> eventDetail = (ArrayList<String>)returnItem.getObj();
                    String summary = eventDetail.get(0);
                    DateTime startingDateTime = new DateTime(eventDetail.get(1));
                    DateTime endingDateTime = new DateTime(eventDetail.get(2));
                    Event newEvent = addEvent(summary,startingDateTime,endingDateTime);
                    System.out.println(returnItem.getObj());
                    System.out.println("In listen function the userName is " + userNameOfCurrentUser);
                    try {
                        String groupCalendarId = UserDAO.getInstanceOf().getGroupCalendar(userNameOfCurrentUser);
                        System.out.println("In listen function the group Calendar id is " + groupCalendarId);
                        eventToCalendar(groupCalendarId, newEvent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
        currentWebsocket = conn;
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
        String[] parts = message.split(",");
        System.out.println(parts);
        for(String string:parts){
            System.out.println(string);
        }

        Command command;

        if(parts[0].equals("okay")){
            System.out.println("Now we are updating the event to another calendar");
            conn.send(groupCalendarIdOfCurrentUser);
            return;
        }
        System.out.println("The part 0 word is " + parts[0]);
        String change = parts[0];
        // User want to change its status in room
        if(change.equals("status")){
            System.out.println("The command is toggle event");
            String userName = parts[1];
            String toggleStatus = parts[2];
            String sendBackMessage = "toggle,";
            userName = userName.replace(userName.substring(userName.length()-1), "");
            System.out.println("The user " + userName + " change the toggle status to " + toggleStatus);
            UserDAO.getInstanceOf().updateStatus(userName,toggleStatus);
            try {
                String houseHandle = UserDAO.getInstanceOf().getHouseHandle(userName);
                System.out.println("The user's house handle is now " + houseHandle);
                ArrayList<User> allUsers = UserDAO.getInstanceOf().getUsers(houseHandle);
                for(User user: allUsers){
                    sendBackMessage += user.getUsername() + ": " + user.getCheckedInStatus() + "<br/>";
                }
                System.out.println(sendBackMessage);
                for(WebSocket webSocket: conns){
                    webSocket.send(sendBackMessage);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        // User want to add/delete an event
        else if(parts[0].equals("eventform")){
            DateTime startingDateTime = new DateTime(parts[2]+":00-07:00");
            DateTime endingDateTime = new DateTime(parts[3]+":00-07:00");
            String summary = parts[1];
            Event event = addEvent(summary,startingDateTime, endingDateTime);
            String calendar = parts[4];
            System.out.println(calendar);
            if(calendar.equals("Group Calendar")){
                eventToCalendar(groupCalendarIdOfCurrentUser,event);
                conn.send(groupCalendarIdOfCurrentUser);
                ArrayList<String> eventDetail = new ArrayList<>();
                eventDetail.add(parts[1]);
                eventDetail.add(parts[2] + ":00-07:00");
                eventDetail.add(parts[3] + ":00-07:00");
                System.out.println("The size of conns is" + conns.size());
                eventToAllCalendar(event);
                for(WebSocket webSocket: conns){
                    if(webSocket != conn) {
                        webSocket.send(parts[1]);
                    }
                }
                return;
            }
            else{
                if(calendar.equals("Class Calendar")){
                    eventToCalendar(classCalendarIdOfCurrentUser,event);
                }else{
                    eventToCalendar(socialCalendarIdOfCurrentUser,event);
                }
            }
        }

        // user want to choose an calendar
        else{
            System.out.println("User choose a calendar");
            System.out.println("and his/her choice is " + parts[1]);
            if (parts[1].equals("class")) {
                classCalendarIdOfCurrentUser = summaryToId.get(parts[2]);
                groupId = parts[3];
                this.groupId = groupId.substring(0, groupId.length() - 1);
                userNameOfCurrentUser = parts[4];
                userNameOfCurrentUser = userNameOfCurrentUser.substring(0, userNameOfCurrentUser.length() - 1);
                UserDAO.getInstanceOf().addClassCalendar(userNameOfCurrentUser,classCalendarIdOfCurrentUser);
            }
            else if(parts[1].equals("social")){
                socialCalendarIdOfCurrentUser = summaryToId.get(parts[2]);
                userNameOfCurrentUser = parts[4];
                UserDAO.getInstanceOf().addSocialCalendar(userNameOfCurrentUser,socialCalendarIdOfCurrentUser);
            }
            else{
                try {
                    groupCalendarIdOfCurrentUser = addCalendar(parts[2]);
                    groupCalendarIdOfCurrentUser = groupCalendarIdOfCurrentUser.replaceAll("@", "%40");
                    userNameOfCurrentUser = parts[3];
                    userNameOfCurrentUser = userNameOfCurrentUser.replace(userNameOfCurrentUser.substring(userNameOfCurrentUser.length()-1), "");
                    System.out.println("The user name is now " + userNameOfCurrentUser);
                    UserDAO.getInstanceOf().addGroupCalendar(userNameOfCurrentUser,groupCalendarIdOfCurrentUser);
                    conn.send(groupCalendarIdOfCurrentUser);
                    ArrayList<String> eventDetail = new ArrayList<>();
                    eventDetail.add("first");

                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("The id of group calendar is " + groupCalendarIdOfCurrentUser);
            }
            System.out.println("Adding/Choosing the calendar");
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            conns.remove(conn);
        }
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /*
    public void updatedStatus(String status){
        //TODO WRITE TO DATABSE & OR / UPDATED HERE?
        roommatesStatus = status;
        sendToggle(roommatesStatus);
    }

    // Toggle
    public void sendToggle(String status){
        Command command = new Command(CommandType.TOGGLE_EVENT, status);
        this.sendObject(command);
    }
*/

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
        if(event == null) {
            System.out.println("The event is null");
        }
        if(calendarId == null)
        {
            System.out.println("The calendar Id is null");
        }
        if(this.service == null){
            System.out.println("The service is null");
        }
        try{
            calendarId = calendarId.replaceAll("%40","@");
            System.out.println("In adding event to calendar function " + calendarId);
            this.service.events().insert(calendarId, event).execute();
            System.out.printf("Event created! ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eventToAllCalendar(Event event){
        String pageToken = null;
        do {
            CalendarList calendarList = null;
            try {
                calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<CalendarListEntry> items = calendarList.getItems();


            for (CalendarListEntry calendarListEntry : items) {
                String id = calendarListEntry.getId();
                String summary = calendarListEntry.getSummary();
                summaryToId.put(summary, id);
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);

        for (String value : summaryToId.values()) {
            eventToCalendar(value, event);
        }
    }

    // Create a new calendar and add it into the current calendar list
    public String addCalendar(String summary) throws Exception
    {
        com.google.api.services.calendar.model.Calendar calendar = new Calendar();
        calendar.setSummary(summary);
        calendar.setTimeZone("America/Los_Angeles");
        Calendar createdCalendar = service.calendars().insert(calendar).execute();
        String calendarId = createdCalendar.getId();
        return calendarId;

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
    }
}