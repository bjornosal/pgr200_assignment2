import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class DatabaseHandler{

    private DatabaseConnection databaseConnection;
    private String propertyFilePath;
    private String subjectFormat = "%-7s| %-40s| %-10s| %-16s| %-9s|";
    private String lecturerFormat = "%-4s| %-15s|";
    private String roomFormat = "%-6s| %-11s| %-15s|";

    protected DatabaseHandler() throws IOException {
        databaseConnection = new DatabaseConnection();
    }


    //TODO do prepared statements at the missing 3 classes.

    private String getResultHeader(String queryType) {
        String result = "";

        switch(queryType) {
            case "subject":
                result += String.format(subjectFormat, "Code", "Name","Students", "Teaching form", "Duration");
                break;
            case "lecturer":
                result += String.format(lecturerFormat, "ID", "Name");
                break;
            case "room":
                result += String.format(roomFormat, "Room", "Type", "Facilities");
                break;
        }
        return result;
    }

    public void tearDownDatabaseAndSetBackUp(File subjectFile, File roomFile, File lecturerFile) throws SQLException, FileNotFoundException {
        dropTable("subject");
        dropTable("room");
        dropTable("lecturer");

        createDatabase();
        createTableWithTableName("subject");
        createTableWithTableName("room");
        createTableWithTableName("lecturer");
        fillTable(subjectFile, "subject");
        fillTable(roomFile, "room");
        fillTable(lecturerFile, "lecturer");
    }

    /*
     * Copied from assignment1
     *
     */

    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    private int getColumnCountOfTable(String tableName) throws SQLException {
        return getResultSetMetaDataForEntireTable(tableName).getColumnCount();
    }

    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public int getRowCountOfTable(String tableName) throws SQLException {
        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        String selectAllQuery = "SELECT * FROM " + tableName + ";";
        int rowCount = 0;

        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectAllQuery);

            while (rs.next()) {
                rowCount++;
            }
        }
        return rowCount;
    }

    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    private String[] getColumnNames(String tableName) throws SQLException {
        String[] columnNames = new String[getColumnCountOfTable(tableName)];
        String query = "SELECT * FROM " + tableName + ";";
        //Shorten code by taking entire try - with resource out of methods??
        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();

        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for(int i = 1; i < getColumnCountOfTable(tableName); i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
            }
        }

        return columnNames;
    }

    //SOURCE: https://stackoverflow.com/questions/12367828/how-can-i-get-different-datatypes-from-resultsetmetadata-in-java
    // Ment to be used to make mostly generic. Resolved using objects instead of 17 if's

    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    private String[] getDataTypes(String tableName) throws SQLException {
        String[] dataTypes = new String[getColumnCountOfTable(tableName)];
        String query = "SELECT * FROM " + tableName + ";";

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for(int i = 1; i < getColumnCountOfTable(tableName); i++) {
                String dataType = rsmd.getColumnTypeName(i);
                dataTypes[i-1] = dataType;
            }
        }
        return dataTypes;
    }

    /**
     *
     * @param tableInformation
     * @param tableName
     * @throws SQLException
     * @throws FileNotFoundException
     */
    private void fillTable(File tableInformation, String tableName) throws SQLException, FileNotFoundException {
        Scanner fileStream = new Scanner(tableInformation);
        fileStream.useDelimiter(";|\\r\\n|\\n");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            String prpStmt = prepareInsertStatement(tableName);

            while (fileStream.hasNext()) {
                PreparedStatement preparedStatement = connection.prepareStatement(prpStmt);
                for(int i = 1; i < getColumnCountOfTable(tableName)+1; i++) {
                    preparedStatement.setObject(i, fileStream.next());
                }
                preparedStatement.executeUpdate();
            }
        }
    }

    /**
     * Puts together a prepared statement based on a table name.
     *
     * @param tableName
     * @return a prepared query
     * @throws SQLException
     */
    private String prepareInsertStatement(String tableName) throws SQLException {
        String preparedStatement = "INSERT INTO " + tableName + " VALUES (";
        int columns = getColumnCountOfTable(tableName);
        for(int i = 1; i < columns; i++) {
            preparedStatement += "?, ";
            if (i == columns - 1) {
                preparedStatement += "?);";
            }
        }
        return preparedStatement;
    }

    /***
     * Implement usage for more generic methods
     * Should return column numbers, maybe column names to skip those?
     *
     * @param rsmd Takes in ResultSetMetaData to se which column number has an autoincrement, for now only one column.
     * @return int
     * @throws SQLException
     */
    private int findAutoIncrement(ResultSetMetaData rsmd) throws SQLException {
        int columnNumber = -1;

        for(int i = 1; i < rsmd.getColumnCount(); i ++) {
            if(rsmd.isAutoIncrement(i)) {
                columnNumber = i;
            }
        }

        return columnNumber;
    }

    public String getSubjectRowBySubjectID(String subjectID) throws SQLException {
        String result = "";
        String query = "SELECT id, name, attending_students, teaching_form, duration \n" +
                "FROM subject\n" +
                "WHERE ID = ?;";

        String[] rowResult = new String[getColumnCountOfTable("subject")];
        result += getResultHeader("subject");
        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, subjectID);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                for(int i = 1; i <= getColumnCountOfTable("subject"); i++) {
                    rowResult[i-1] = rs.getObject(i).toString();
                    if(i == getColumnCountOfTable("subject")) {
                        result += "\n";
                    }
                }
                result += String.format(subjectFormat,
                        rowResult[0], rowResult[1], rowResult[2], rowResult[3], rowResult[4]);
            }
        }
        return result;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public String getAllRowsFromSubjectTable() throws SQLException {
        String result = "";
        String query = "SELECT id, name, attending_students, teaching_form, duration\n" +
                "FROM subject;";
        String[] rowResult = new String[getColumnCountOfTable("subject")];
        result += getResultHeader("subject");
        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();


        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                for(int i = 1; i <= getColumnCountOfTable("subject"); i++) {
                  rowResult[i-1] = rs.getObject(i).toString();

                    if(i == getColumnCountOfTable("subject")) {
                        result += "\n";
                    }
                }
                result += String.format(subjectFormat,
                        rowResult[0], rowResult[1], rowResult[2], rowResult[3], rowResult[4]);
            }

        }
        return result;
    }

    /**
     *
     * @param name
     * @return
     * @throws SQLException
     */
    public String getLecturerRowByName(String name) throws SQLException {
        String result = "";
        String query = "SELECT id, name\n" +
                "FROM lecturer\n" +
                "WHERE name = ?";

        String[] rowResult = new String[getColumnCountOfTable("lecturer")];
        result += getResultHeader("lecturer");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                for(int i = 1; i <= getColumnCountOfTable("lecturer"); i++) {
                    rowResult[i-1] = rs.getObject(i).toString();
                    if(i == getColumnCountOfTable("lecturer")) {
                        result += "\n";
                    }
                }
                result += String.format(lecturerFormat,
                        rowResult[0], rowResult[1]);
            }
        }
        return result;
    }
//TODO All the queries can be a lot more dynamic, more high cohesion method wise
    public String getAllRowsFromLecturerTable() throws SQLException {
        String result = "";
        //Query has to be dynamic, and not a static choice of columns
        String query = "SELECT id, name \n" +
                "FROM lecturer;";
        String[] rowResult = new String[getColumnCountOfTable("lecturer")];
        result += getResultHeader("lecturer");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                for(int i = 1; i <= getColumnCountOfTable("lecturer"); i++) {
                    rowResult[i-1] = rs.getObject(i).toString();
                    if(i == getColumnCountOfTable("lecturer")) {
                        result += "\n";
                    }
                }
                result += String.format(lecturerFormat,
                        rowResult[0], rowResult[1]);
            }
        }
        return result;

    }

    /**
     *
     * @param roomName
     * @return
     * @throws SQLException
     */
    public String getRoomRowByName(String roomName) throws SQLException {
        String result = "";
        String query = "SELECT name, type, facilities\n" +
                "FROM room\n" +
                "WHERE name = ?;";
        String[] rowResult = new String[getColumnCountOfTable("room")];
        result += getResultHeader("room");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, roomName);
            ResultSet rs = preparedStatement.executeQuery();
            //TODO implement check if amount of rows in this resultSet is less than 1
            //TODO do it in all

            while(rs.next()) {
                for(int i = 1; i <= getColumnCountOfTable("room"); i++) {
                    rowResult[i-1] = rs.getObject(i).toString();
                    if(i == getColumnCountOfTable("room")) {
                        result += "\n";
                    }
                }
                result += String.format(roomFormat,
                        rowResult[0], rowResult[1], rowResult[2]);
            }
        }
        return result;

    }


    public String getAllRowsFromRoomTable() throws SQLException {
        String result = "";
        String query = "SELECT name, type, facilities\n" +
                "FROM room;";
        String[] rowResult = new String[getColumnCountOfTable("room")];
        result += getResultHeader("room");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                for(int i = 1; i <= getColumnCountOfTable("room"); i++) {
                    rowResult[i-1] = rs.getObject(i).toString();
                    if(i == getColumnCountOfTable("room")) {
                        result += "\n";
                    }
                }
                result += String.format(roomFormat,
                        rowResult[0], rowResult[1], rowResult[2]);
            }
        }
        return result;

    }

    /**
     * Helping method for getting metadata for a table
     * @param tableName
     * @return metadata of a resultset for table of name param
     * @throws SQLException
     */
    private ResultSetMetaData getResultSetMetaDataForEntireTable(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + ";";
        ResultSetMetaData resultSetMetaData;

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            resultSetMetaData = rs.getMetaData();
        }
        return resultSetMetaData;
    }


    /**
     *
     * @param tableName
     * @throws SQLException
     */
    private void createTableWithTableName(String tableName) throws SQLException {
        switch (tableName) {
            case "subject":
                createSubjectTable();
                break;
            case "lecturer":
                createLecturerTable();
                break;
            case "room":
                createRoomTable();
                break;
        }
    }

    //Not in use as of right now
    private boolean checkIfTableExists(String tableName) throws SQLException {
        boolean exists = false;
        try (Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
            if(rs.next()) {
                exists = true;
            }
        }
        return exists;
    }

    protected void dropTable(String tableName) throws SQLException {
        try(Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS "+tableName);
        }
    }

    private void createDatabase() throws SQLException{
        try (Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE SCHEMA IF NOT EXISTS pgr200_assignment_1;");
        }
    }

    private void createSubjectTable() throws SQLException {
        try(Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS subject (\n" +
                    "id VARCHAR(255) UNIQUE,\n" +
                    "name varchar(255) UNIQUE NOT NULL,\n" +
                    "attending_students INT(6),\n" +
                    "teaching_form varchar(50) NOT NULL,\n" +
                    "duration DECIMAL(11),\n" +
                    "PRIMARY KEY(id));");
        }
    }

    private void createLecturerTable() throws SQLException {
        try(Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS lecturer (\n" +
                    "id int(11) auto_increment,\n" +
                    "name varchar (255),\n" +
                    "PRIMARY KEY (id)\n" +
                    ");");
        }
    }

    private void createRoomTable() throws SQLException {
        try (Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS room (\n" +
                    "name varchar(255) UNIQUE, \n" +
                    "type ENUM('SMALLROOM', 'LARGEROOM', 'LARGEAUD', 'SMALLAUD'),\n" +
                    "facilities varchar(255)\n" +
                    ");");
        }
    }


    /*
     * ^ Copied from assignment 1
     *
     */
    protected DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    protected String getPropertyFilePath() {
        return propertyFilePath;
    }

    protected void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }

    protected void startDatabase() throws IOException {
        databaseConnection.setUpDatabase(getPropertyFilePath());

    }


}