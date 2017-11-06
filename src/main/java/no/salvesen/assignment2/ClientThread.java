package no.salvesen.assignment2;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class ClientThread implements Runnable
{
    private Socket threadSocket;
    private int id;
    private InputHandler inputHandler;

    public ClientThread(int id, Socket socket) throws IOException {
        setId(id);
        threadSocket = socket;
    }

    public void run()
    {
        try (PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
             BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()))
        ) {

            while (true) {
                inputHandler = new InputHandler(outputToClient, inputFromClient);
                inputHandler.startMenuLoop();
            }
            //TODO sort exception handling here
        } catch(SocketException e) {
            System.out.println("Client disconnected");
        } catch(IOException exception) {
            System.out.println("###########: " + exception);
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