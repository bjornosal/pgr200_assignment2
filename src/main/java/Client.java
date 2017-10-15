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
        try {
            Socket socket = new Socket("localhost",5000);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //This will wait for the server to send the string to the client saying a connection
            //has been made.

         /*   if(input.ready()) {
                String receivedMsg = input.readLine();
                System.out.println(receivedMsg);
            }*/

            //Again, here is the code that will run the client, this will continue looking for
            //input from the user then it will send that info to the server.
            while(true) {
                //Here we look for input from the user
                BufferedReader userTerminal = new BufferedReader(new InputStreamReader(System.in));
                if(userTerminal.ready()) {
                    String userInput = userTerminal.readLine();
                    //Now we write it to the server/client
                    output.println(userInput);
                }

                if(input.ready()) {
                    String receivedMsg = input.readLine();
                    System.out.println(receivedMsg);
                }
            }
        } catch (IOException exception) {
            System.out.println("Feilmelding: " + exception);
        }
    }
}