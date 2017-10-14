import java.net.*;
import java.util.*;
import java.io.*;

public class ServerClient
{
    public static void main(String[] args)
    {
        new ServerClient();
    }

    private ArrayList<Client> clients;

    public ServerClient()
    {
               startServer();
    }

    private void startServer() {
        //TODO Add ArrayList so that it is possible to keep track of clients
        //We need a try-catch because lots of errors can be thrown

        try {
            ServerSocket sSocket = new ServerSocket(5000);
            System.out.println("Server started at: " + new Date());



            //Neeed to be able to send information from the server to the client

            //Loop that runs server function
            while(true) {
                //Server accepts all incoming clients
                Socket socket = sSocket.accept();
                //Creates custom thread for each client
                ClientThread clientThread = new ClientThread(socket);
                //Starts the thread
                new Thread(clientThread).start();
                //End thread functions

                //Sending information to the user ->
                BufferedReader serverTerminal = new BufferedReader(new InputStreamReader(System.in));
                String serverInput = serverTerminal.readLine();




            }
        } catch(IOException exception) {
            System.out.println("Error: " + exception);
        }
    }

}