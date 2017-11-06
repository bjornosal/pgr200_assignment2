package no.salvesen.assignment2;

public class ExceptionHandler {


    /**
     * Prints out an error message to the user depending on where in the code the issue was presented.
     * @param errorLocation Location in code error was thrown.
     */
    public String outputIOException(String errorLocation) {
        switch(errorLocation) {
            case "WriteProp":
                return "Issues writing properties to file.";
            case "connect":
                return "There was an issue with the database name";
        }
        return "";
    }

    /**
     * Prints out an error message if a file could not be found.
     */
    public String outputFileNotFoundException() {
        return "Could not find file(s). Please check that file(s) is present.";
    }

    /**
     * Prints out an error message to the user depending on where in the code the issue was presented.
     * @param errorLocation Location in code error was thrown.
     */
    public String outputSQLException(String errorLocation) {
        switch(errorLocation) {
            case "connect":
                return "There was an issue connecting to the database with the properties-file. \nPlease try again.";
            case "createTable":
                return "There was an issue creating table with information in the file.";
        }
        return "Some error with SQL. Unable to locate location of error.";
    }

    public String outputDatabaseSQLException() {

        return "ERROR:\nPlease check that database is set up prior to connecting. \nIf database is set up, please check username and password.";
    }

}
