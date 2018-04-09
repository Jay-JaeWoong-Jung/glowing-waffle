package servlet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Vector;

import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/ws")
public class Server{
    private static HashMap<String, ServerThread> threadMap = new HashMap<>();
    private Vector<ServerThread> serverThreads;
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
                serverThreads.add(st); //TODO ADD THE THREAD INTO THE SERVERTHREAD
            }
        } catch (IOException ioe) {
            System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
        }
    }

    public void processCommand(Command command, ServerThread thread){
        switch (command.getCommandType()){
            case GROUP_EVENT:
                groupEvent(command, thread);
            case TOGGLE_EVNET:
                toggleEvent(command,thread);
        }

    }

    public void toggleEvent(Command command, ServerThread thread){
        //broadcast(command, thread);
    }

    public void groupEvent(Command command, ServerThread thread){
        // TODO
        System.out.println("Received a group event");
        //Event to be broadcasted ;
        //broadcast(command,thread);
    }

    // broadcast function
    /*
    public void broadcast(servlet.Command command, servlet.ServerThread thread) {
        //if (message != null) {
        if (command != null) {
            //System.out.println(message);

            for(servlet.ServerThread threads : serverThreads) {
                if (thread != threads) {
                    //threads.sendMessage(message);
                    threads.sendObject(command);
                }
            }
        }
    }
    */

}