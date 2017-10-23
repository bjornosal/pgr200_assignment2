import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread implements Runnable
{
    private Socket threadSocket;
    private int id;
    private InputHandler inputHandler = new InputHandler();

    public ClientThread(int id, Socket socket) throws IOException {
        setId(id);
        threadSocket = socket;
    }

    public void run()
    {
        try (PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
             BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));
//             BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(System.in))
        ) {

            while (true) {
             /*   if(inputFromClient.ready()) {
                    String fromClient = inputFromClient.readLine();
                    System.out.println(fromClient);
                }
                if(inputFromServer.ready()) {
                    String fromServer = inputFromServer.readLine();
                    outputToClient.println(fromServer);
                }*/

                //TODO Implement propertyFiles for each client using userID
                inputHandler.startInputHandler(outputToClient,inputFromClient);
            }
        } catch(IOException exception) {
            System.out.println("Feilmelding: " + exception);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}