import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private Vector<ServerThread> serverThreads;

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
                serverThreads.add(st);
            }
        } catch (IOException ioe) {
            System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
        }
    }

    public void processCommand(Command command, ServerThread thread){
        switch (command.getCommandType()){
            case GROUP_EVENT:
                groupEvent(command, thread);
        }

    }

    public void groupEvent(Command command, ServerThread thread){
        // TO-DO
    }

    // broadcast function

}
