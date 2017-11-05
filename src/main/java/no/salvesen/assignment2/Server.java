package no.salvesen.assignment2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class Server {

    public static void main(String[] args) {
        new Server();
    }

    private Server(){



        try(ServerSocket sSocket = new ServerSocket(8888)) {
            System.out.println("Server started at " + new Date());
            HashMap<Integer, ClientThread> clientMap = new HashMap<>();
            int clientCounter = 0;
            System.out.println("Connected clients: " + clientMap.size());

            //TODO decrease clientCounter if a client disconnects
            while (true){
                Socket socket = sSocket.accept();
                ClientThread clientThread = new ClientThread(clientCounter, socket);
                new Thread(clientThread).start();
                clientCounter++;
                clientMap.put(clientThread.getId(), clientThread);
                System.out.println("Clients that has connected: " + clientMap.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


