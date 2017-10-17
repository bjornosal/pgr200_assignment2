/*public class Server
{
    public static void main(String[] args)
    {
        new Server();
    }

    // Using hashmap to easily identify users by searching by username (to be implemented) to find \n
    // user and print to the correct one.
    //Use both ip and username as key
    // using integer for now
    private Map<Integer, ClientThread> clients = new HashMap<>();
    private PrintWriter output;
    private BufferedReader serverInput;
    public Server()
    {
        runServer();
    }

    private void runServer() {
        //TODO Add ArrayList so that it is possible to keep track of clients

        try {
            ServerSocket sSocket = new ServerSocket(5000);
            System.out.println("Server started at: " + new Date());

            // Need to be able to send information from the server to the client
            int counter = 0;
            // Loop that runs server function
            while(true) {
                //Server accepts all incoming clients
                Socket socket = sSocket.accept();


                //Starting reader and writer
                output = new PrintWriter(socket.getOutputStream(), true);
                serverInput = new BufferedReader(new InputStreamReader(System.in));



                //Creates custom thread for each client
                ClientThread clientThread = new ClientThread(socket);
                //Starts the thread
                new Thread(clientThread).start();
                //Generate the key
                clients.put(counter,clientThread);
                //TODO fix
                counter++;
                //Needs to keep track of clients though? Or is it automaticly linked.
                //TODO: How does the server choose which client gets message?? By using HashMap key?
                // to keep a chat going with one thread, just insert a while here
                if (serverInput.ready()) {
                    String input = serverInput.readLine();
                    output.println(input);
                    //clientThread.setMessageFromServer(input);
                }

            }
        } catch(IOException exception) {
            System.out.println("Feilmelding: " + exception);
        }
    }

    private String getMenuString() {
        String result = String.format("32%s", "Velkommen");
        return result;
    }*/


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class Server {

    private HashMap<Integer, ClientThread> clientMap;
    int counter;

    public static void main(String[] args) {
        new Server();
    }

    private Server(){



        try(ServerSocket sSocket = new ServerSocket(8888)) {
            System.out.println("Server started at " + new Date());
            clientMap = new HashMap<>();
            counter = 0;
            System.out.println("Connected clients: " + clientMap.size());

            while (true){
                Socket socket = sSocket.accept();
                ClientThread clientThread = new ClientThread(counter, socket);
                new Thread(clientThread).start();
                counter++;
                clientMap.put(clientThread.getId(), clientThread);
                System.out.println("Connected clients: " + clientMap.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


