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

            //TODO implement decrease if client disconnects, as that can be used for testing.
            //TODO decrease clientCounter if a client disconnects
            //TODO fix unknownHostConnection osv
            while (true){
                Socket socket = sSocket.accept();
                ClientThread clientThread = new ClientThread(clientCounter, socket);
                new Thread(clientThread).start();
                clientCounter++;
                printInformationForThread(socket, clientThread);
                clientMap.put(clientThread.getId(), clientThread);
                System.out.println("Clients that has connected: " + clientMap.size());
            }
        } catch (IOException e) {
            System.out.println("#####");
            e.printStackTrace();
        }
    }

    private void printInformationForThread(Socket socket, ClientThread clientThread) {
        System.out.println("Client connected:");
        System.out.println("IP address: " + socket.getInetAddress());
        System.out.println("Thread ID: " + clientThread.getId());
    }
}


