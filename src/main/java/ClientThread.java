import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private Socket threadSocket;
    private int id;
    private InputHandler inputHandler = new InputHandler();
    private MysqlDataSource dataSource;

    public ClientThread(int id, Socket socket)
    {
        setId(id);
        threadSocket = socket;
        dataSource = new MysqlDataSource();
    }

    public void run()
    {
        try (PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
             BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
             BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
             /*   if(inputFromClient.ready()) {
                    String fromClient = inputFromClient.readLine();
                    System.out.println(fromClient);
                }
                if(inputFromServer.ready()) {
                    String fromServer = inputFromServer.readLine();
                    outputToClient.println(fromServer);
                }*/
                boolean hasSentMainMenu = false;
                boolean propertiesIsSet = false;
                if(!hasSentMainMenu) {
                    //TODO Implement propertyFiles for each client using userID
                    inputHandler.setUpProperties(outputToClient,inputFromClient);
                    //Enters loop in menu
                    inputHandler.startMenuLoop(outputToClient, inputFromClient);



                }
            }
        } catch(IOException exception) {
            System.out.println("Feilmelding: " + exception);
        }
    }

    //TODO add menuHeader

    //TODO add responses

    //TODO add databaseConnection

    //TODO add databaseHandler

    //TODO add


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}