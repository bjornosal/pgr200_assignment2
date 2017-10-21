
import java.io.*;
import java.util.Properties;

public class InputHandler {

    public InputHandler() {
    }

    private Menu menu = new Menu();
    private DatabaseConnection databaseConnection = new DatabaseConnection();

    public void setUpProperties(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException {
        boolean finished = false;
        String menuChoice;

        while (!finished) {
            Properties properties = new Properties();
            outputToClient.println(menu.propertiesMenu());
            menuChoice = inputFromClient.readLine();
            if (menuChoice.equals("1") || menuChoice.equals("2") || menuChoice.equals("3")) {

                //Use default properties
                if (menuChoice.equals("1")) {
                    databaseConnection.setDatabaseProperties("./src/files/defaultDatabaseLogin.properties");
                    finished = true;
                    //use properties previously set by user
                } else if(menuChoice.equals("2")) {
                    databaseConnection.setDatabaseProperties("./src/files/userEnteredDatabaseLogin.properties");
                    finished = true;
                    //Enter new properties
                } else {
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
                        databaseConnection.setDatabaseProperties("./src/files/userEnteredDatabaseLogin.properties");
                        finished = true;
                    }
                }
            } else {
                outputToClient.println("Incorrect choice, please try again.");
            }
        }
    }


}