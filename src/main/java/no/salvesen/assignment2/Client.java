package no.salvesen.assignment2;

import java.io.*;
import java.net.*;

public class Client
{
    public static void main(String[] args)
    {
        new Client();
    }

    public Client()
    {
        try(Socket socket = new Socket("localhost",8888);
            PrintWriter outputToThread = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(System.in))) {

            while(true) {
                if(inputFromClient.ready()) {
                    String input = inputFromClient.readLine();
                    outputToThread.println(input);
                }

                if(inputFromServer.ready()) {
                    String messageReceivedFromServer = inputFromServer.readLine();
                    if(messageReceivedFromServer.equals("CLOSE_SOCKET")) {
                        outputToThread.println("CLOSING_CONNECTION");
                        socket.close();
                        break;
                    } else {
                        System.out.println(messageReceivedFromServer);
                    }
                }
            }
            //TODO fix exception handling here
        } catch (IOException exception) {
            System.out.println("Feilmelding: " + exception);
        }
    }
}