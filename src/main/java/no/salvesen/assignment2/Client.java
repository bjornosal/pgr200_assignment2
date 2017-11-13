package no.salvesen.assignment2;

import java.io.*;
import java.net.*;

/**
 * The type Client.
 */
public class Client
{
    private ExceptionHandler exceptionHandler;

    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 8888;

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

            while (true) {

                forwardMessageFromClient(outputToServer, inputFromClient);


                messageFromServer(inputFromServer, socket);
            }
        }catch(IOException e) {
            exceptionHandler.outputIOException("message");
        }
    }

    /**
     * Forward message from client.
     *
     * @param outputToServer  the output to server
     * @param inputFromClient the input from client
     */
    protected void forwardMessageFromClient(PrintWriter outputToServer, BufferedReader inputFromClient) {
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
     * Message from server.
     *
     * @param inputFromServer the input from server
     * @param socket          the socket
     */
    protected void messageFromServer(BufferedReader inputFromServer, Socket socket)  {
        try {
            if (inputFromServer.ready()) {
                String messageReceivedFromServer = inputFromServer.readLine();

                if (messageReceivedFromServer.equals("CLOSE_SOCKET")) {
                    socket.close();
                } else {
                    System.out.println(messageReceivedFromServer);
                }
            }
        } catch (IOException e) {
            exceptionHandler.outputIOException("message");
        }
    }
}