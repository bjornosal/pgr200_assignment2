import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ClientThread implements Runnable
{
    Socket threadSocket;


    public ClientThread(Socket socket)
    {
        threadSocket = socket;
    }


    public void run()
    {
        try {
            //Create the streams
            //ServerInput
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));

            //Client input
            //Two streams for each, two for client and two for server?
            PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));

            //Tell the client that he/she has connected
            //Is this from the server?
            outputToClient.println("You have connected at: " + new Date());

            while (true) {
                //This will wait until a line of text has been sent
                String chatInput = inputFromClient.readLine();
                System.out.println(chatInput);

            }
        } catch(IOException exception) {
            System.out.println("Error: " + exception);
        }
    }
}