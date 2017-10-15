import java.net.*;
import java.util.*;
import java.io.*;

public class ServerClient
{
    public static void main(String[] args)
    {
        new ServerClient();
    }

    // Using hashmap to easily identify users by searching by username (to be implemented) to find \n
    // user and print to the correct one.
    //Use both ip and username as key
    // using integer for now
    private Map<Integer, ClientThread> clients = new HashMap<>();
    private PrintWriter output;
    private BufferedReader serverInput;
    public ServerClient()
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
    }
/*
public void fileChooser() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please choose which table you are entering information for: ");
        System.out.println("1: Subject");
        System.out.println("2: Room");
        System.out.println("3: Lecturer");
        System.out.println("4: Use existing files in files folder");
        setTablePick(userInput.nextInt());


        switch (tablePick) {
            case 1:
                setTableName("subject");
                break;
            case 2:
                setTableName("room");
                break;
            case 3:
                setTableName("lecturer");
                break;
            case 4:
                System.out.println("Existing files chosen.");
                break;
            default:
                System.out.println("Existing files chosen.");
                break;

        }
        if(tablePick == 1 || tablePick == 2 || tablePick == 3) {
            System.out.println("Please copy the file-path to the csv file for " + tableName + "s.");
            //To get input correctly.
            userInput.nextLine();
            setFilePath(userInput.nextLine());
        }

        switch (tablePick) {
            case 1:
                setSubjectFile(new File(filePath));
                setCurrentFile(getSubjectFile());
                break;
            case 2:
                setRoomFile(new File(filePath));
                setCurrentFile(getRoomFile());
                break;
            case 3:
                setLecturerFile(new File(filePath));
                setCurrentFile(getLecturerFile());
                break;
            case 4:
                setSubjectFile(new File("src/files/subject.csv"));
                setRoomFile(new File("src/files/room.csv"));
                setLecturerFile(new File("src/files/lecturer.csv"));
                setCurrentFile(getSubjectFile());
                break;
            default:
                setSubjectFile(new File("src/files/subject.csv"));
                setRoomFile(new File("src/files/room.csv"));
                setLecturerFile(new File("src/files/lecturer.csv"));
                setCurrentFile(getSubjectFile());
                break;
        }
    }
 */
}