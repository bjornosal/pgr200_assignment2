package no.salvesen.assignment2;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Client.
 */
public class Client
{
    private ExceptionHandler exceptionHandler;

    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 8888;

    private boolean clientIsConnected = true;
    private String propertiesFilePath;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        new Client();
    }

    /**
     * Instantiates a new Client.
     */
    public Client()
    {
        exceptionHandler = new ExceptionHandler();

        try(Socket socket = new Socket(SERVER_HOST,SERVER_PORT);
            PrintWriter outputToServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(System.in))) {

            while(clientIsConnected) {
                forwardMessageFromClient(outputToServer, inputFromClient);
                messageFromServer(inputFromServer, socket);
            }
        } catch(IOException e) {
            exceptionHandler.outputIOException("message");
        }
    }

    /**
     * Forward message from client.
     *
     * @param outputToServer  the output to server
     * @param inputFromClient the input from client
     */
    private void forwardMessageFromClient(PrintWriter outputToServer, BufferedReader inputFromClient) {
        try {
            if (inputFromClient.ready()) {
                String input = inputFromClient.readLine();
                outputToServer.println(input);
            }
        } catch(IOException e){
            exceptionHandler.outputIOException("message");
        }

    }

    /**
     * Prints message from server.
     *
     * @param inputFromServer the input from server
     * @param socket          the socket
     */
    private void messageFromServer(BufferedReader inputFromServer, Socket socket)  {
        try {
            if (inputFromServer.ready()) {
                String messageReceivedFromServer = inputFromServer.readLine();

                if (messageReceivedFromServer.equals("CLOSE_SOCKET")) {
                    System.out.println("Have a nice day!");
                    socket.close();
                    clientIsConnected = false;
                } else {
                    System.out.println(messageReceivedFromServer);
                }
            }
        } catch (IOException e) {
            exceptionHandler.outputIOException("message");
        }
    }
}