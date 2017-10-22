import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class InputHandler {


    private Menu menu;
    private DatabaseHandler databaseHandler;

    private File subjectFile;
    private File roomFile;
    private File lecturerFile;

    public InputHandler() throws IOException {
        setSubjectFile(new File("src/files/subject.csv"));
        setRoomFile(new File("src/files/room.csv"));
        setLecturerFile(new File("src/files/lecturer.csv"));

        databaseHandler = new DatabaseHandler();
        menu = new Menu();
    }

    public void startInputHandler(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        startMenuLoop(outputToClient,inputFromClient);
    }

    public void setUpProperties(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException {
        boolean finished = false;
        String menuChoice;

        while (!finished) {
            Properties properties = new Properties();
            outputToClient.println(menu.propertiesMenu());
            menuChoice = inputFromClient.readLine();
            switch(menuChoice) {

                //Use default properties
                case "1":
                    databaseHandler.setPropertyFilePath("./src/files/defaultDatabaseLogin.properties");
                    finished = true;
                    break;

                //use properties previously set by user
                case "2":
                    databaseHandler.setPropertyFilePath("./src/files/userEnteredDatabaseLogin.properties");
                    finished = true;
                    break;

                //Enter new properties
                case "3":
                    outputToClient.println("Server name: ");
                    String serverName = inputFromClient.readLine();
                    outputToClient.println("Database name: ");
                    String databaseName = inputFromClient.readLine();
                    outputToClient.println("Username: ");
                    String databaseUser = inputFromClient.readLine();
                    outputToClient.println("Password: ");
                    String databasePassword = inputFromClient.readLine();

                    //Filling property file
                    properties.setProperty("serverName", serverName);
                    properties.setProperty("databaseName", databaseName);
                    properties.setProperty("databaseUser", databaseUser);
                    properties.setProperty("databasePassword", databasePassword);
                    File userEnteredProperties = new File("./src/files/userEnteredDatabaseLogin.properties");

                    try(FileOutputStream fileOut = new FileOutputStream(userEnteredProperties)) {
                        properties.store(fileOut, "Added by user");
                        outputToClient.println("Property file set up. Attempting to connect.\n");
                        databaseHandler.setPropertyFilePath("./src/files/userEnteredDatabaseLogin.properties");
                        finished = true;
                    }
                    break;

                default:
                    outputToClient.println("Incorrect choice, please try again.");
                    break;

            }
        }
        databaseHandler.startDatabase();
    }

    public void startMenuLoop(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        setUpProperties(outputToClient, inputFromClient);
        showMainMenu(outputToClient, inputFromClient);
    }

    private void showMainMenu(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        String menuChoice;
        while(true) {
            outputToClient.println(menu.mainMenu());
            menuChoice = inputFromClient.readLine();
            System.out.println(databaseHandler.getPropertyFilePath());

            switch(menuChoice) {
                case "1":
                    showSearchMenu(outputToClient, inputFromClient);
                    break;
                case "2":
                    showTableMenu(outputToClient, inputFromClient);
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }
        }
    }

    private void showTableMenu(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        String menuChoice;
        while(true) {
            outputToClient.println(menu.tableMenu());
            menuChoice = inputFromClient.readLine();
            String filePathMessage = "Please enter the file-path to the csv file.";

            switch(menuChoice) {
                case "1":
                    outputToClient.println(filePathMessage);
                    setSubjectFile(new File(inputFromClient.readLine()));
                    break;
                case "2":
                    outputToClient.println(filePathMessage);
                    setRoomFile(new File(inputFromClient.readLine()));
                    break;
                case "3":
                    outputToClient.println(filePathMessage);
                    setLecturerFile(new File(inputFromClient.readLine()));
                    break;
                case "4":
                    outputToClient.println("Existing files chosen");
                    break;
                case "5":
                    showMainMenu(outputToClient, inputFromClient);
                    break;
                case "6":
                    //Closes thread
//                    Thread.currentThread().interrupt();
//                    return;
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }

        }
    }

    private void showSearchMenu(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        String menuChoice;
        while(true) {
            outputToClient.println(menu.searchMenu());
            menuChoice = inputFromClient.readLine();

            switch(menuChoice) {
                case "1":
                    outputToClient.println("Subject code: ");
                    String subject = inputFromClient.readLine();
                    outputToClient.println(databaseHandler.getSubject(subject));
                    break;
                case "2":
                    outputToClient.println(databaseHandler.getAllSubjects());
                    break;
                case "3":
                    //Get information on all subjects
                    //add this method in databasehandler
                    break;
                case "4":
                    //Get information on all subjects
                    //add this method in databasehandler
                    break;
                case "5":
                    //Get information on all subjects
                    //add this method in databasehandler
                    break;
                case "6":
                    //Get information on all subjects
                    //add this method in databasehandler
                    break;
                case "7":
                    showMainMenu(outputToClient,inputFromClient);
                    break;
                case "8":
                    //Closes thread
//                    Thread.currentThread().interrupt();
//                    return;
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }

        }
    }


    public File getSubjectFile() {
        return subjectFile;
    }

    public void setSubjectFile(File subjectFile) {
        this.subjectFile = subjectFile;
    }

    public File getRoomFile() {
        return roomFile;
    }

    public void setRoomFile(File roomFile) {
        this.roomFile = roomFile;
    }

    public File getLecturerFile() {
        return lecturerFile;
    }

    public void setLecturerFile(File lecturerFile) {
        this.lecturerFile = lecturerFile;
    }
}