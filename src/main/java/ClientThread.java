import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ClientThread implements Runnable
{
    private Socket threadSocket;
    private String messageFromServer = "";

    public ClientThread(Socket socket)
    {
        threadSocket = socket;
    }


    public void run()
    {
        try {
            //Create the streams
            //ServerInput
            //BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));

            //Client input
            //Two streams for each, two for client and two for server?
            PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));

            while (true) {
                //Check if user has sent anything.
                //This will wait until a line of text has been sent
                if(inputFromClient.ready()) {
                    String chatInput = inputFromClient.readLine();
                    System.out.println(chatInput);
                }

                if(!messageFromServer.equals("")) {
                    setMessageFromServer("");
                }


                //Need to check if server has sent anything. Message from server does
                // not go through client at the moment




            }
        } catch(IOException exception) {
            System.out.println("Error: " + exception);
        }
    }

    public void setMessageFromServer(String messageFromServer) {
        this.messageFromServer = messageFromServer;
    }
}