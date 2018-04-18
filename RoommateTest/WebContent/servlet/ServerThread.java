package servlet;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    private PrintWriter pw;
    private BufferedReader br;
    private Server server;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ServerThread(Socket s, Server server) {
        try {
            this.server = server;
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            this.start();

        } catch (IOException ioe) {
            System.out.println("ioe in servlet.ServerThread constructor: " + ioe.getMessage());
        }
    }


    public void sendObject(Command command) {
        try {
            oos.writeObject(command);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                Command command = (Command) ois.readObject();
                server.processCommand(command,this);
            }
        } catch (IOException ioe) {
            System.out.println("ioe in servlet.ServerThread.run(): " + ioe.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
