import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private Socket threadSocket;
    private int id;

    public ClientThread(int id, Socket socket)
    {
        this.id = id;
        threadSocket = socket;
    }


    public void run()
    {
        try (PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
             BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
             BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                if(inputFromClient.ready()) {
                    String chatInput = inputFromClient.readLine();
                    System.out.println(chatInput);
                }
                if(inputFromServer.ready()) {
                    String server = inputFromServer.readLine();
                    outputToClient.println(server);
                }
            }
        } catch(IOException exception) {
            System.out.println("Feilmelding: " + exception);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}