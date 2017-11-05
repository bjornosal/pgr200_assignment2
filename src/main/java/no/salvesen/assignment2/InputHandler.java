package no.salvesen.assignment2;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class InputHandler {


    //TODO Fix exception throws etc
    //TODO put files in a property file maybe?
    private Menu menu;
    private DatabaseHandler databaseHandler;
    private FileReader fileReader;


    public InputHandler() throws IOException {
        fileReader = new FileReader();



        databaseHandler = new DatabaseHandler();
        menu = new Menu();
    }


    private void setUpProperties(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
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

        //Starts database with the properties chosen.
        databaseHandler.startConnection();

    }

    public void startMenuLoop(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        setUpProperties(outputToClient, inputFromClient);

        //Temporary setup for testing
        databaseHandler.tearDownDatabaseAndSetBackUp();

        showMainMenu(outputToClient, inputFromClient);
    }


    private void showMainMenu(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        String menuChoice;
        while(true) {
            outputToClient.println(menu.mainMenu());
            menuChoice = inputFromClient.readLine();

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
                    fileReader.setSubjectFile(new File(inputFromClient.readLine()));
                    break;
                case "2":
                    outputToClient.println(filePathMessage);
                    fileReader.setRoomFile(new File(inputFromClient.readLine()));
                    break;
                case "3":
                    outputToClient.println(filePathMessage);
                    fileReader.setLecturerFile(new File(inputFromClient.readLine()));
                    break;
                case "4":
                    outputToClient.println("Existing files chosen");
                    break;
                case "5":
                    chooseTableToFillWithInformation(outputToClient, inputFromClient);
                    break;
                case "6":
                    showMainMenu(outputToClient, inputFromClient);
                    break;
                case "7":
                    outputToClient.println("quit");
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
                    outputToClient.println("Please enter subject code: ");
                    String subject = inputFromClient.readLine();
                    outputToClient.println(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("subject","code",subject));
                    break;
                case "2":
                    outputToClient.println(databaseHandler.getAllRowsByTableName("subject"));
                    break;
                case "3":
                    outputToClient.println("Please enter name of lecturer: ");
                    String lecturer = inputFromClient.readLine();
                    outputToClient.println(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("lecturer","name",lecturer));
                    break;
                case "4":
                    outputToClient.println(databaseHandler.getAllRowsByTableName("lecturer"));
                    break;
                case "5":
                    outputToClient.println("Please enter name of room: ");
                    String room = inputFromClient.readLine();
                    outputToClient.println(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("room","name",room));
                    break;
                case "6":
                    outputToClient.println(databaseHandler.getAllRowsByTableName("room"));
                    break;
                case "7":
                    showMainMenu(outputToClient,inputFromClient);
                    break;
                case "8":
                    outputToClient.println("quit");
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }

        }
    }

    /**
     *
     * Prints out a list of possible tables to choose from.
     * Until a correct table is chosen, will stay in loop.
     * @param outputToClient PrintWriter To send string to client
     * @param inputFromClient BufferReader to read input from the user.
     * @throws SQLException If unable to get connection.
     * @throws FileNotFoundException If unable to find the file.
     */
    private void chooseTableToFillWithInformation(PrintWriter outputToClient, BufferedReader inputFromClient) throws SQLException, IOException {
        String chosenTable;
        while (true) {
            outputToClient.println("Possible tables are: ");
            for(String tableName : databaseHandler.getArrayListOfTableNames()) {
                outputToClient.println(tableName);
            }
            outputToClient.println("Write return to return to main menu.");
            chosenTable = inputFromClient.readLine();
            for (String tableName : databaseHandler.getArrayListOfTableNames()) {
                if (chosenTable.equals(tableName)) {
                    databaseHandler.tearDownTableAndSetBackUpWithNewInformation(chosenTable);
                    outputToClient.print("Cleared tables and filled with information from files.\n");
                    return;
                }
            }
            if(chosenTable.equalsIgnoreCase("return")) {
                return;
            }
        }
    }
}