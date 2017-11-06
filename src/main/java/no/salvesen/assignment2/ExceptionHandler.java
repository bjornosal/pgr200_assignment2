package no.salvesen.assignment2;

public class ExceptionHandler {

    /**
     * Prints out an error message to the user depending on where in the code the issue was presented.
     * @param errorLocation Location in code error was thrown.
     */
    public void outputIOException(String errorLocation) {
        switch(errorLocation) {
            case "WriteProp":
                System.out.println("Issues writing properties to file.");
                break;
            case "connect":
                System.out.println("There was an issue with the database name");
                break;
        }
    }

    /**
     * Prints out an error message if a file could not be found.
     */
    public void outputFileNotFoundException() {
        System.out.println("Could not find file(s). Please check that file(s) is present.");
    }

    /**
     * Prints out an error message to the user depending on where in the code the issue was presented.
     * @param errorLocation Location in code error was thrown.
     */
    public void outputSQLException(String errorLocation) {
        switch(errorLocation) {
            case "connect":
                System.out.println("There was an issue connecting to the database with the properties-file. \nPlease try again.");
                break;
            case "createTable":
                System.out.println("There was an issue creating table with information in the file.");
                break;
        }
    }
}
