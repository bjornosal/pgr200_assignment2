package no.salvesen.assignment2;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class Server {

    public static void main(String[] args) {
        new Server();
    }

    /**
     * Instantiates a new Server.
     */
    public Server(){
        try(ServerSocket sSocket = new ServerSocket(8888)) {
            System.out.println("Server started at " + new Date());
            int clientCounter = 0;

            while (true){
                Socket socket = sSocket.accept();
                ClientThread clientThread = new ClientThread(clientCounter, socket);
                new Thread(clientThread).start();

                clientCounter++;
                System.out.println("Client number " + clientCounter + " connected with: ");
                printInformationForThread(socket, clientThread);
            }
        } catch (SocketException e) {
            System.out.println("Issue with socket.");
        } catch (IOException e) {
            System.out.println("Issue with file.");
        }
    }

    /**
     *
     * @param socket Socket that the client will receive.
     * @param clientThread The thread created by the server.
     */
    private void printInformationForThread(Socket socket, ClientThread clientThread) {
        System.out.println("IP address: " + socket.getInetAddress());
        System.out.println("Thread ID: " + clientThread.getId());
    }
}


