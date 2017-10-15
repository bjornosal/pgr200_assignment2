import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private Socket threadSocket;

    public ClientThread(Socket socket)
    {
        threadSocket = socket;
    }


    public void run()
    {
        try (

                PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
                BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()))) {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                if(inputFromClient.ready()) {
                    String chatInput = inputFromClient.readLine();
                    System.out.println(chatInput);
                }
                if(serverInput.ready()) {
                    String server = serverInput.readLine();
                    outputToClient.println(server);
                }
            }
        } catch(IOException exception) {
            System.out.println("Error: " + exception);
        }
    }

}