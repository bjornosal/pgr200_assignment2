package no.salvesen.assignment2;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class InputHandler {


    //TODO Fix exception throws etc
    private Menu menu;
    private DatabaseHandler databaseHandler;
    private FileReader fileReader;
    private ExceptionHandler exceptionHandler;

    private PrintWriter outputToClient;
    private BufferedReader inputFromClient;


    public InputHandler(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        fileReader = new FileReader();
        exceptionHandler = new ExceptionHandler();
        databaseHandler = new DatabaseHandler();
        menu = new Menu();
        this.outputToClient = outputToClient;
        this.inputFromClient = inputFromClient;
    }

    //TODO Add Javadoc
    public void setUpProperties() throws IOException, SQLException {
        boolean finished = false;
        String menuChoice;

        while (!finished) {
            Properties properties = new Properties();
            outputToClient.println(menu.propertiesMenu());
            menuChoice = inputFromClient.readLine();
            switch (menuChoice) {

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
                    setUserProperties(properties);
                    finished = true;
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
                    break;

            }
        }

        //Starts database with the properties chosen.

        databaseHandler.startConnection();

        //Handling the issues of wrong properties
        try {
            databaseHandler.createDatabase();
        } catch (SQLException e) {
            outputDatabaseExceptionOccurred();
            setUpProperties();
        }
    }

    public void startMenuLoop() throws IOException, SQLException {
        setUpProperties();
        outputToClient.println("Connected to database.");
        databaseHandler.tearDownDatabaseAndSetBackUp();
        showMainMenu();
    }


    private void showMainMenu() throws IOException, SQLException {
        String menuChoice;
        while (true) {
            outputToClient.println(menu.mainMenu());
            menuChoice = inputFromClient.readLine();

            if (menuChoice != null) {
                switch (menuChoice) {
                    case "1":
                        showSearchMenu();
                        break;
                    case "2":
                        showTableMenu();
                        break;
                    default:
                        outputToClient.println("Incorrect choice, please try again.");
                }
            }
        }
    }

    private void showTableMenu() throws IOException, SQLException {
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
                    outputToClient.println(filePathMessage);
                    fileReader.setLecturer_in_subject_file(new File(inputFromClient.readLine()));
                    break;
                case "5":
                    outputToClient.println("Existing files chosen");
                    break;
                case "6":
                    chooseTableToFillWithInformation();
                    break;
                case "7":
                    showMainMenu();
                    break;
                case "8":
                    outputToClient.println("CLOSE_SOCKET");
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }

        }
    }

    private void showSearchMenu() throws IOException, SQLException {
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
                    outputToClient.println("Getting information on subjects with lecturer.");
                    outputToClient.println(databaseHandler.getSubjectNameAndLecturerNameBasedOnPrimaryKeys());
                    break;
                case "8":
                    showMainMenu();
                    break;
                case "9":
                    outputToClient.println("CLOSE_SOCKET");
                    return;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }

        }
    }

    /**
     *
     * Prints out a list of possible tables to choose from.
     * Until a correct table is chosen, will stay in loop.
     * @throws SQLException If unable to get connection.
     * @throws FileNotFoundException If unable to find the file.
     */
    private void chooseTableToFillWithInformation() throws SQLException, IOException {
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
                    if(checkIfDependentOnLinkTable(chosenTable)) {
                        System.out.println("Lecturer_in_subject table requires this table.");
                        System.out.println("Tear down and set it back up together with " + tableName + "-table ?");
                        System.out.println("Y/N");
                        if(inputFromClient.readLine().equalsIgnoreCase("Y")) {
                            databaseHandler.tearDownTableAndSetBackUpWithNewInformation("lecturer_in_subject");
                        } else {
                            break;
                        }
                    }
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

    private boolean checkIfDependentOnLinkTable(String tableName) throws FileNotFoundException, SQLException {
        if (tableName.equalsIgnoreCase("subject") || tableName.equalsIgnoreCase("lecturer")) {
            return true;
        }
        return false;
    }

    private void outputDatabaseExceptionOccurred() {
        outputToClient.println(exceptionHandler.outputDatabaseSQLException());
    }

    private void setUserProperties(Properties properties) throws IOException {
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

        try (FileOutputStream fileOut = new FileOutputStream(userEnteredProperties)) {
            properties.store(fileOut, "Added by user");
            outputToClient.println("Property file set up. Attempting to connect.\n");
            databaseHandler.setPropertyFilePath("./src/files/userEnteredDatabaseLogin.properties");

        }
    }
}