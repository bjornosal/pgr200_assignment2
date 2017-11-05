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
                    System.out.println(messageReceivedFromServer);
                }
            }
        } catch (IOException exception) {
            System.out.println("Feilmelding: " + exception);
        }
    }
}