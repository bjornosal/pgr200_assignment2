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

            //Again, here is the code that will run the client, this will continue looking for
            //input from the user then it will send that info to the server.
            while(true) {
                //Here we look for input from the user
                if(inputFromClient.ready()) {
                    String input = inputFromClient.readLine();
                    //Now we write it to the server/client
                    outputToThread.println(input);
                }

                if(inputFromServer.ready()) {
                    String receivedMsg = inputFromServer.readLine();
                    System.out.println(receivedMsg);
                }
            }
        } catch (IOException exception) {
            System.out.println("Feilmelding: " + exception);
        }
    }
}