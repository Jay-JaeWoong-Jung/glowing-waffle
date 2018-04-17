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

    // Group Websocket storage
    // Group Calendar storage
    HashMap<String, Set<WebSocket>> webSocketController = new HashMap<>();
    HashMap<String, Set<String>> groupCalendarController = new HashMap<>();

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

        if(parts[0].equals("okay")){
            System.out.println("Now we are updating the event to another calendar");
            conn.send(groupCalendarIdOfCurrentUser);
            return;
        }

        String change = parts[0];

        // User want to change its status in room（Fully Success)
        if(change.equals("status")){
            System.out.println("The command is toggle event");
            String userName = parts[1];
            String toggleStatus = parts[2];
            String sendBackMessage = "toggle,";
            userName = userName.replace(userName.substring(userName.length()-1), "");
            String houseId = parts[3];
            houseId = houseId.substring(0, houseId.length() - 1);

            // check for websocket set
            Set<WebSocket> value = webSocketController.get(houseId);
            if(value!=null){
                System.out.println("");
                if(!value.contains(conn)){
                    value.add(conn);
                }
            } else{
                System.out.println("The current house id doesn't exists");
                Set<WebSocket> webSocketCollection = new HashSet<>();
                webSocketCollection.add(conn);
                webSocketController.put(houseId,webSocketCollection);
            }

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

                Set<WebSocket> toThisHouse = webSocketController.get(houseId);
                for(WebSocket websocket: toThisHouse){
                    websocket.send(sendBackMessage);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        // User want to add/delete an event
        else if(parts[0].equals("eventform")){
            DateTime startingDateTime = new DateTime(parts[3]+":00-07:00");
            DateTime endingDateTime = new DateTime(parts[4]+":00-07:00");
            String summary = parts[2];
            Event event = addEvent(summary,startingDateTime, endingDateTime);
            String calendar = parts[5];
            System.out.println(calendar);
            String houseHandle = parts[6];
            houseHandle = houseHandle.substring(0, houseHandle.length() - 1);
            groupCalendarIdOfCurrentUser = parts[1];
            System.out.println("Current group id is " + houseHandle);
            if(calendar.equals("Group Calendar")){
                // get the group calendar of the current socket
                //groupCalendarIdOfCurrentUser = parts[1];

                // check whether the group id exist; if not then add the group calendar controller;
                Set<String> value = groupCalendarController.get(houseHandle);
                if(value!=null){
                    System.out.println("The current house id exists");
                    if(!value.contains(groupCalendarIdOfCurrentUser)){
                        value.add(groupCalendarIdOfCurrentUser);
                    }
                } else{
                    System.out.println("The current house id doesn't exists");
                    Set<String> idCollection = new HashSet<>();
                    idCollection.add(groupCalendarIdOfCurrentUser);
                    groupCalendarController.put(houseHandle,idCollection);
                }

                System.out.println("We are now adding a group event ");
                System.out.println("The group calendar is " + groupCalendarIdOfCurrentUser);
                conn.send(groupCalendarIdOfCurrentUser);
                ArrayList<String> eventDetail = new ArrayList<>();
                eventDetail.add(parts[2]);
                eventDetail.add(parts[3] + ":00-07:00");
                eventDetail.add(parts[4] + ":00-07:00");

                System.out.println("The size of conns is" + conns.size());

                ArrayList<String> broadCastTarget = UserDAO.getInstanceOf().getGroupCalendars(houseHandle);
                for(String getAllCalendar:broadCastTarget){
                    if(getAllCalendar != null){
                        System.out.println("In boradCastTarget the calendar is " + getAllCalendar);
                        eventToCalendar(getAllCalendar,event);
                    }
                }

                Set<WebSocket> toThisHouse = webSocketController.get(houseHandle);
                for(WebSocket websocket: toThisHouse){
                    websocket.send(parts[1]);
                }

                return;
            }
            else{
                    eventToCalendar(groupCalendarIdOfCurrentUser,event);
                    conn.send(parts[1]);
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


    public static void main(String[] args) throws IOException {
        Client player = new Client( "Username","localhost", 6789);
    }
}