package no.salvesen.assignment2;

/**
 * The type Exception handler.
 */
public class ExceptionHandler {


    /**
     * Output io exception string.
     *
     * @param errorLocation the error location
     * @return the string
     */
    public String outputIOException(String errorLocation) {
        switch(errorLocation.toLowerCase()) {
            case "writeprop":
                return "Issues writing properties to file.";
            case "connect":
                return "There was an issue with the database name";
            case "fileissue":
                return "There was an issue with the property file.";
            case "message":
                return "There was an issue with the message forwarding.";
        }
        return "An issue with IO. Unable to locate location of error.";
    }


    /**
     * Output file not found exception string.
     *
     * @return the string
     */
    public String outputFileNotFoundException() {
        return "Could not find file(s). Please check that file(s) is present.";
    }


    /**
     * Output sql exception string.
     *
     * @param errorLocation the error location
     * @return the string
     */
    public String outputSQLException(String errorLocation) {
        switch(errorLocation.toLowerCase()) {
            case "connect":
                return "There was an issue connecting to the database with the properties-file. \nPlease try again.";
            case "createTable":
                return "There was an issue creating table with information in the file.";
            case "createdatabase":
                return "There was an issue with the database.";
            case "foreignkey":
                return "User is having issues with foreign key constraint";
        }
        return "Some error with SQL. Unable to locate location of error.";
    }

    /**
     * Output database sql exception string.
     *
     * @return the string
     */
    public String outputDatabaseSQLException() {

        return "ERROR:\nPlease check that database is set up prior to connecting. \nIf database is set up, please check username and password.";
    }

}
