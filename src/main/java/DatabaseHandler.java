import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class DatabaseHandler{

    private DatabaseConnection databaseConnection;
    private String propertyFilePath;
    private String subjectFormat = "%-7s| %-40s| %-10s| %-15s| %-9s|";
    private String lecturerFormat = "%-4s| %-15s|";
    private String roomFormat = "%-6s| %-11s| %-15s|";

    protected DatabaseHandler() throws IOException {
        databaseConnection = new DatabaseConnection();

    }

    private String getResultHeader(String queryType) {
        String result = "";

        switch(queryType) {
            case "subject":
                result += String.format(subjectFormat, "Code", "Name","Students", "Teachingform", "Duration");
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

    /*
     * Copied from assignment1
     *
     */
    public int getColumnCount(String tableName) throws SQLException {
        return getFullResultSetMetaData(tableName).getColumnCount();
    }

    public int getRowCount(String tableName) throws SQLException {
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

    private String[] getColumnNames(String tableName) throws SQLException {
        String[] columnNames = new String[getColumnCount(tableName)];
        String query = "SELECT * FROM " + tableName + ";";
        //Shorten code by taking entire try - with resource out of methods??
        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();

        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for(int i = 1; i < getColumnCount(tableName); i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
            }
        }

        return columnNames;
    }

    //SOURCE: https://stackoverflow.com/questions/12367828/how-can-i-get-different-datatypes-from-resultsetmetadata-in-java
    // Ment to be used to make mostly generic. Resolved using objects instead of 17 if's
    private String[] getDataTypes(String tableName) throws SQLException {
        String[] dataTypes = new String[getColumnCount(tableName)];
        String query = "SELECT * FROM " + tableName + ";";

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for(int i = 1; i < getColumnCount(tableName); i++) {
                String dataType = rsmd.getColumnTypeName(i);
                dataTypes[i-1] = dataType;
            }
        }
        return dataTypes;
    }

    public void fillTable(File tableInformation, String tableName) throws SQLException, FileNotFoundException {
        Scanner fileStream = new Scanner(tableInformation);
        fileStream.useDelimiter(";|\\r\\n|\\n");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            String prpStmt = prepareInsertStatement(tableName);

            while (fileStream.hasNext()) {
                PreparedStatement preparedStatement = connection.prepareStatement(prpStmt);
                for(int i = 1; i < getColumnCount(tableName)+1; i++) {
                    preparedStatement.setObject(i, fileStream.next());
                }
                preparedStatement.executeUpdate();
            }
        }
    }

    private String prepareInsertStatement(String tableName) throws SQLException {
        String prpStatement = "INSERT INTO " + tableName + " VALUES (";
        int columns = getColumnCount(tableName);
        for(int i = 1; i < columns; i++) {
            prpStatement += "?, ";
            if (i == columns - 1) {
                prpStatement += "?);";
            }
        }
        return prpStatement;
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

    public String getSubject(String subject) throws SQLException {
        String result = "";
        String query = "SELECT id as 'Kode', name as 'Navn', attending_students as 'Studenter', teaching_form as 'Laeringsform', duration as 'Lengde'\n" +
                "FROM subject\n" +
                "WHERE id = '"+ subject + "';";

        String[] rowResult = new String[getColumnCount("subject")];
        result += getResultHeader("subject");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                for(int i = 1; i <= getColumnCount("subject"); i++) {
                    rowResult[i-1] = rs.getObject(i).toString();
                    if(i == getColumnCount("subject")) {
                        result += "\n";
                    }
                }
                result += String.format(subjectFormat,
                        rowResult[0], rowResult[1], rowResult[2], rowResult[3], rowResult[4]);
            }
        }
        return result;
    }

    public String getAllSubjects() throws SQLException {
        String result = "";
        String query = "SELECT id as 'Code', name as 'Navn', attending_students as 'Studenter', teaching_form as 'Laeringsform', duration as 'Lengde'\n" +
                "FROM subject;";
        String[] rowResult = new String[getColumnCount("subject")];
        result += getResultHeader("subject");
        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                for(int i = 1; i <= getColumnCount("subject"); i++) {
                  rowResult[i-1] = rs.getObject(i).toString();

                    if(i == getColumnCount("subject")) {
                        result += "\n";
                    }
                }
                result += String.format(subjectFormat,
                        rowResult[0], rowResult[1], rowResult[2], rowResult[3], rowResult[4]);
            }

        }
        return result;
    }


    private ResultSetMetaData getFullResultSetMetaData(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + ";";
        ResultSetMetaData rsmd;

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
        }
        return rsmd;
    }


    /**
     * This part is methods that creates all stuff required.
     *
     *
     */
    protected void createTable(String tableName) throws SQLException {
        switch (tableName) {
            case "subject":
                createSubjectTable();
                break;
        }
    }

    //Not in use as of right now
    private boolean checkIfTableExists(String tableName) throws SQLException {
        boolean exists = false;
        try (Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
            System.out.println(rs);
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
        String tableName = "subject";
        try(Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                    "id VARCHAR(255) UNIQUE,\n" +
                    "name varchar(255) UNIQUE NOT NULL,\n" +
                    "attending_students INT(6),\n" +
                    "teaching_form varchar(50) NOT NULL,\n" +
                    // Issue with data truncation might lie here.
                    //Mysql has issues with float, use decimal instead.
                    "duration DECIMAL(11),\n" +
                    //"duration FLOAT(11),\n" +
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
                    "type varchar(255),\n" +
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