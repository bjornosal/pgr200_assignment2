package no.salvesen.assignment2;

import java.io.*;
import java.net.*;

public class Client
{
    private ExceptionHandler exceptionHandler;

    public static void main(String[] args)
    {
        new Client();
    }

    public Client()
    {
        exceptionHandler = new ExceptionHandler();

        try(Socket socket = new Socket("localhost",8888);
            PrintWriter outputToServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(System.in))) {

            while(true) {
                if(inputFromClient.ready()) {
                    forwardMessageFromClient(outputToServer, inputFromClient);
                }

                if(inputFromServer.ready()) {
                    forwardMessageFromServer(inputFromServer, socket);
                }
            }
        }  catch (IOException e) {
            exceptionHandler.outputIOException("message");
        }
    }

    public void forwardMessageFromClient(PrintWriter outputToServer, BufferedReader inputFromClient) {
        String input = null;
        try {
            input = inputFromClient.readLine();
        } catch (IOException e) {
            exceptionHandler.outputIOException("message");
        }
        outputToServer.println(input);
    }

    public void forwardMessageFromServer(BufferedReader inputFromServer, Socket socket)  {
        String messageReceivedFromServer = null;
        try {
            messageReceivedFromServer = inputFromServer.readLine();
        } catch (IOException e) {
            exceptionHandler.outputIOException("message");
        }
        if(messageReceivedFromServer.equals("CLOSE_SOCKET")) {
            try {
                socket.close();
            } catch (IOException e) {
                exceptionHandler.outputIOException("message");
            }
        } else {
            System.out.println(messageReceivedFromServer);
        }
    }
}