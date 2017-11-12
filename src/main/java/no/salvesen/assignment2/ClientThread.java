package no.salvesen.assignment2;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class ClientThread implements Runnable
{
    private Socket threadSocket;
    private int id;
    private ExceptionHandler exceptionHandler;

    public ClientThread(int id, Socket socket) throws IOException {
        setId(id);
        threadSocket = socket;
        exceptionHandler = new ExceptionHandler();
    }

    public void run()
    {
        try (PrintWriter outputToClient = new PrintWriter(threadSocket.getOutputStream(), true);
             BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()))
        ) {
            InputHandler inputHandler = new InputHandler(outputToClient, inputFromClient);
            inputHandler.startMenuLoop();
        } catch(SocketException e) {
            System.out.println("Client disconnected");
        } catch(IOException exception) {
            System.out.println(exceptionHandler.outputIOException("There is an issue with the file."));
        } catch (SQLException e) {
            System.out.println(exceptionHandler.outputSQLException("foreignkey"));
        }
    }


    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

}