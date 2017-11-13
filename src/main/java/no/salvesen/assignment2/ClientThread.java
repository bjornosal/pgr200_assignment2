package no.salvesen.assignment2;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

/**
 * The type Client thread.
 */
public class ClientThread implements Runnable
{
    private Socket threadSocket;
    private int id;
    private ExceptionHandler exceptionHandler;
    private String sessionPropertiesFileName;

    /**
     * Instantiates the ClientThread.
     *
     * @param id     the id
     * @param socket the socket
     * @throws IOException the io exception
     */
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
            generateSessionPropertiesFileName();

            InputHandler inputHandler = new InputHandler(outputToClient, inputFromClient, sessionPropertiesFileName);
            inputHandler.startMenuLoop();
            //TODO delete propertiesfile
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

    private void generateSessionPropertiesFileName() {
        sessionPropertiesFileName = "propertyFileFor_" + getId() + threadSocket.getInetAddress();
    }

}