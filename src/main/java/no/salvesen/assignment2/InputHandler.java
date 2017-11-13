package no.salvesen.assignment2;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The type Input handler.
 */
public class InputHandler {


    private Menu menu;
    private DatabaseHandler databaseHandler;
    private FileReader fileReader;
    private ExceptionHandler exceptionHandler;
    private PropertiesHandler propertiesHandler;

    private String sessionPropertiesFilePath;
    private PrintWriter outputToClient;
    private BufferedReader inputFromClient;
    private boolean connected = true;

    /**
     * Instantiates a new Input handler.
     *
     * @param outputToClient  the output to client
     * @param inputFromClient the input from client
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public InputHandler(PrintWriter outputToClient, BufferedReader inputFromClient, String sessionPropertiesFilePath) throws IOException, SQLException {
        fileReader = new FileReader();
        exceptionHandler = new ExceptionHandler();
        menu = new Menu();
        propertiesHandler = new PropertiesHandler();
        databaseHandler = new DatabaseHandler(propertiesHandler);

        this.sessionPropertiesFilePath = sessionPropertiesFilePath;
        this.outputToClient = outputToClient;
        this.inputFromClient = inputFromClient;
    }

    /**
     * Used for setting up properties.
     * @throws IOException If issues with file.
     * @throws SQLException if issue with SQL queries.
     */
    public void setUpProperties() throws IOException, SQLException {
        boolean finished = false;
        String menuChoice;

        while (!finished && connected) {
            Properties properties = new Properties();
            outputToClient.println(menu.propertiesMenu());
            menuChoice = inputFromClient.readLine();
            switch (menuChoice) {
                case "1":
                    if(!isDefaultDatabaseLoginPropertiesFileIsEmpty()) {
                        outputToClient.println("Default login has not been set up,\nplease set up before continuing.");
                        setUserProperties(properties);
                    }
                    else {
                        propertiesHandler.setPropertyFilePath("./src/files/defaultDatabaseLogin.properties");
                    }
                    finished = true;
                    break;
                case "2":
                    setUserProperties(properties);
                    finished = true;
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
                    break;

            }
        }
        databaseHandler.startConnection();

        try {
            databaseHandler.createDatabase();
        } catch (SQLException e) {
            exceptionHandler.outputDatabaseSQLException();
            setUpProperties();
        }
    }

    /**
     * Starts the application loop,
     * and handles exceptions thrown by the methods.
     */
    public void startMenuLoop() {
        try {
            setUpProperties();
        } catch (IOException e) {
            exceptionHandler.outputIOException("writeprop");
        } catch(SQLException e) {
            exceptionHandler.outputIOException("createdatabase");
        }
        try {
            databaseHandler.tearDownDatabaseAndSetBackUp();
            connected = true;
        } catch (SQLException e) {
            exceptionHandler.outputSQLException("connect");
        } catch (FileNotFoundException e) {
            exceptionHandler.outputFileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            showMainMenu();
        } catch (SQLException e) {
            exceptionHandler.outputSQLException("createtable");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            exceptionHandler.outputFileNotFoundException();
        } catch(IOException e) {
            exceptionHandler.outputIOException("fileissue");
        }
    }

    /**
     * Main menu loop with two choices.
     * @throws IOException If issue with printing.
     * @throws SQLException If issue with queries further down.
     */
    private void showMainMenu() throws IOException, SQLException {
        String menuChoice;
        while (connected) {
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

    /**
     * Shows the table menu
     * @throws IOException if issue with file or printing.
     * @throws SQLException if issue with SQL queries.
     */
    private void showTableMenu() throws IOException, SQLException {
        String menuChoice;
        while(connected) {
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

    /**
     * Shows the search menu.
     * @throws IOException If issue with file or sending message.
     * @throws SQLException If issue with queries further down.
     */
    private void showSearchMenu() throws IOException, SQLException {
        String menuChoice;
        while(connected) {
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
                    setConnected(false);
                    return;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }
        }
    }

    /**
     * Prints out a list of possible tables to choose from.
     * Until a correct table is chosen, will stay in loop.
     * @throws SQLException If unable to get connection.
     * @throws FileNotFoundException If unable to find the file.
     */
    private void chooseTableToFillWithInformation() throws SQLException, IOException {
        String chosenTable;
        while (connected) {
            outputToClient.println("Possible tables are: ");
            for(String tableName : databaseHandler.getArrayListOfTableNames()) {
                outputToClient.println(tableName);
            }
            outputToClient.println("Write return to return to main menu.");
            chosenTable = inputFromClient.readLine();
            for (String tableName : databaseHandler.getArrayListOfTableNames()) {
                if (chosenTable.equals(tableName)) {
                    if(checkIfDependentForLinkTable(chosenTable)) {
                        outputToClient.println("Lecturer_in_subject table requires this table.");
                        outputToClient.println("Tear down and set it back up together with " + tableName + "-table ?");
                        outputToClient.println("Y/N");
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

    /**
     * Checks if the link table is depending on the table.
     * @param tableName table which might be depending.
     * @return true if dependant
     * @throws FileNotFoundException if issue with file.
     * @throws SQLException if issue with queries.
     */
    private boolean checkIfDependentForLinkTable(String tableName) throws FileNotFoundException, SQLException {
        if (tableName.equalsIgnoreCase("subject") || tableName.equalsIgnoreCase("lecturer")) {
            return true;
        }
        return false;
    }

    /**
     * Used to set up the user properties if the user chooses to do so him/herself.
     * @param properties Properties that is being set up in caller method.
     * @throws IOException If issue with file or printing.
     */
    private void setUserProperties(Properties properties) throws IOException {
        outputToClient.println("Server name: ");
        String serverName = inputFromClient.readLine();
        outputToClient.println("Database name: ");
        String databaseName = inputFromClient.readLine();
        outputToClient.println("Username: ");
        String databaseUser = inputFromClient.readLine();
        outputToClient.println("Password: ");
        String databasePassword = inputFromClient.readLine();

        properties.setProperty("serverName", serverName);
        properties.setProperty("databaseName", databaseName);
        properties.setProperty("databaseUser", databaseUser);
        properties.setProperty("databasePassword", databasePassword);
        File chosenTypeOfLogin;

        outputToClient.println("Use these properties as default? Y/N");
        if(inputFromClient.readLine().equalsIgnoreCase("Y")) {
            chosenTypeOfLogin = new File("src/files/defaultDatabaseLogin.properties");
        } else {
            chosenTypeOfLogin = new File(sessionPropertiesFilePath);
        }

        try (FileOutputStream fileOut = new FileOutputStream(chosenTypeOfLogin)) {
            properties.store(fileOut, "Added by user");
            outputToClient.println("Property file set up. Attempting to connect.\n");
            propertiesHandler.setPropertyFilePath(chosenTypeOfLogin.getPath());
        }
    }

    private boolean isDefaultDatabaseLoginPropertiesFileIsEmpty() {
        File defaultDatabaseLogin = new File("src/files/defaultDatabaseLogin.properties");
        return defaultDatabaseLogin.length() > 0;
    }

    /**
     * Sets the connected status of the client.
     * @param connected false if disconnecting.
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}