package servlet;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Vector;

import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/ws")
public class Server{
    private Vector<ServerThread> serverThreads;
    private HashMap<Integer, String> userNameMap = new HashMap<>();
    private HashMap<String, Vector<ServerThread>> broadCastMap = new HashMap<>() ;

    public static void main(String[] args){
        Server server = new Server(6789);

    }

    public Server(int port) {
        try {
            System.out.println("Binding to port " + port);
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Bound to port " + port);
            serverThreads = new Vector<ServerThread>();
            while (true) {
                Socket s = ss.accept(); // blocking
                System.out.println("Connection from: " + s.getInetAddress());
                ServerThread st = new ServerThread(s, this);
                System.out.println("The new server thread is " + st.getId());
                serverThreads.add(st);
            }
        } catch (IOException ioe) {
            System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
        }
    }

    public void processCommand(Command command, ServerThread thread){
        switch (command.getCommandType()){
            case GROUP_EVENT:
                System.out.println("The server received a command called group event");
                groupEvent(command, thread);
            case TOGGLE_EVENT:
                toggleEvent(command,thread);
           // case USERNAME_TO_SERVER:
             //   addToGroup(command, thread);
        }

    }

    /*
    public void addToGroup(Command command, ServerThread thread){
        System.out.println("Now we are inserting userName");
        userNameMap.put(thread,(String)command.getObj());
    }
    */

    public void toggleEvent(Command command, ServerThread thread){
        //broadcast(command, thread);
    }

    public void groupEvent(Command command, ServerThread thread){
        System.out.println("This command comes from " + thread.getId());
        System.out.println("We have in total " + serverThreads.size());
        if(thread.equals(serverThreads.elementAt(0))){
            System.out.println("It is the 1st element");
        } else if(thread.equals(serverThreads.elementAt(1))){
            System.out.println("It is the 2nd element");
        }

        System.out.println("Received a group event");
        ArrayList<String> eventDetail = (ArrayList<String>)command.getObj();
        String userName = (String)command.getObj2();
        System.out.println("The current user is " + userName);
        if(eventDetail.get(0).equals("first"))
        {
            userNameMap.put(serverThreads.indexOf(thread),(String)command.getObj2());
        }
        else{
            command = new Command(CommandType.GROUP_EVENT, eventDetail);
            broadcast(thread, command);
        }

    }

    // broadcast function
    public void broadcast(ServerThread thread, Command command) {
        System.out.println("There is currently " + serverThreads.size());
        System.out.println("The name of currentThread is " + serverThreads.indexOf(thread));


        if (command != null) {
            for(ServerThread allGroupThread : serverThreads) {
                System.out.println("There are " + serverThreads.size() + " inside of this group threads ");
                if(allGroupThread.isAlive() && allGroupThread != thread){
                    ArrayList<String> eventDetail = (ArrayList<String>)command.getObj();
                    String userName = userNameMap.get(allGroupThread);
                    System.out.println("I am sending to the thread identifier known as " + thread.getName());
                    command = new Command(CommandType.GROUP_EVENT, eventDetail);
                    allGroupThread.sendObject(command);
                }
            }
        }


    }
}