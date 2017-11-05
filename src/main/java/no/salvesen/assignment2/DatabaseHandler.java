package no.salvesen.assignment2;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseHandler {

    private DatabaseConnection databaseConnection;
    private FileReader fileReader;
    private String propertyFilePath;
    private ArrayList<String> foreignKeysToBeAdded;

    public DatabaseHandler() {
        databaseConnection = new DatabaseConnection();
        fileReader = new FileReader();
        foreignKeysToBeAdded = new ArrayList<>();

    }

    /**
     * Removes all existing tables and recreate them.
     * Then fills them with data from the files.
     *
     * @throws SQLException If an issue with table or with database.
     * @throws IOException  If an issue with File or InputStream.
     */
    public void tearDownDatabaseAndSetBackUp() throws SQLException, IOException {

        String subjectTable = "subject";
        String roomTable = "room";
        String lecturerTable = "lecturer";

        dropTable(subjectTable);
        dropTable(roomTable);
        dropTable(lecturerTable);

        createDatabase();

        createTableFromMetaData(subjectTable);
        createTableFromMetaData(roomTable);
        createTableFromMetaData(lecturerTable);

        addAllForeignKeysToTables();

        fillTableFromFileByTableName(subjectTable);
        fillTableFromFileByTableName(roomTable);
        fillTableFromFileByTableName(lecturerTable);

    }

    /**
     * Removes a table based on param and sets it back up.
     *
     * @param tableName Table to be dropped and recreated
     * @throws SQLException          If unable to drop table, or unable to run query
     * @throws FileNotFoundException If unable to find the file containing table information.
     */
    public void tearDownTableAndSetBackUpWithNewInformation(String tableName) throws SQLException, FileNotFoundException {
        dropTable(tableName);
        createTableFromMetaData(tableName);
        fillTableFromFileByTableName(tableName);
    }

    /**
     * Creates a list of all the table names in the database
     *
     * @return ArrayList containing table names.
     * @throws SQLException If unable to get a connection.
     */
    public ArrayList<String> getArrayListOfTableNames() throws SQLException {

        ArrayList<String> tableNames = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet rs = databaseMetaData.getTables(null, null, "%", null);
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        }
        return tableNames;
    }


    /**
     * Gets the column count from the ResultSetMetaData
     *
     * @param tableName Which table to get column count for.
     * @return Integer Amount of columns.
     * @throws SQLException If unable to get a connection
     */
    private int getColumnCountOfTable(String tableName) throws SQLException {
        return getResultSetMetaDataForEntireTable(tableName).getColumnCount();
    }


    /**
     * Creates a String that can be used for a prepared statement.
     *
     * @param tableName Which table to prepare insert query for
     * @return String A string that is ready to be set as a prepared query
     * @throws FileNotFoundException If unable to find a file at the specified location.
     */
    private String prepareInsertStatementBasedOnMetaData(String tableName) throws FileNotFoundException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));

        StringBuilder preparedStatement = new StringBuilder();
        preparedStatement.append("INSERT INTO ").append(fileReader.getTableName()).append("\nVALUES (");

        for (int i = 1; i < fileReader.getTableColumnCount(); i++) {
            preparedStatement.append("?, ");
            if (i == fileReader.getTableColumnCount() - 1) {
                preparedStatement.append("?);");
            }
        }
        return preparedStatement.toString();
    }

    /**
     * Fills a table with information from the related table file.
     *
     * @param tableName Which table to fill with information.
     * @throws FileNotFoundException If unable to find file.
     * @throws SQLException          If unable to get a connection to the database.
     */
    private void fillTableFromFileByTableName(String tableName) throws FileNotFoundException, SQLException {
        File tableFile = fileReader.getFileByTableName(tableName);
        fileReader.readFile(tableFile);

        ArrayList<String> insertionValues = fileReader.getInsertionValues();

        try (Connection connection = this.databaseConnection.getConnection()) {
            String preparedInsert = prepareInsertStatementBasedOnMetaData(tableName);
            PreparedStatement preparedStatement = connection.prepareStatement(preparedInsert);

            int index = 1;
            int counter = 0;

            while (counter < insertionValues.size()) {
                while (index < fileReader.getTableColumnCount() + 1) {
                    preparedStatement.setObject(index, insertionValues.get(counter));
                    index++;
                    counter++;
                }
                preparedStatement.addBatch();
                index = 1;
            }
            preparedStatement.executeBatch();
        }
    }

    /**
     * Adding foreign keys to tables after all tables have been created.
     *
     * @throws SQLException If unable to get a connection.
     */
    private void addAllForeignKeysToTables() throws SQLException {
        try (Connection connection = databaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            for (String foreignKeyQuery : foreignKeysToBeAdded) {
                statement.addBatch(foreignKeyQuery);
            }
            statement.executeBatch();

        }
    }

    /**
     * Retrieves all rows from a table based on the column name.
     * Used to find a specific row based on primary keys for queries.
     *
     * @param tableName   Which table to search in.
     * @param columnName  Which column to search in.
     * @param columnValue What value to look for in that column.
     * @return Returns entire row based on columnValue.
     * @throws FileNotFoundException If unable to find the file that it uses to read files.
     * @throws SQLException          If unable to get a connection.
     */
    public String getRowsFromTableByColumnNameAndSearchColumnValue(String tableName, String columnName, String columnValue) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String result = "";
        String query = buildSelectQuery(true, tableName, columnName);

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, columnValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            result += resultStringBuilder(resultSet);
        }
        return result;
    }

    /**
     * Builds a select query, based on parameters.
     *
     * @param isSpecifiedSearch Boolean, if true, adds a WHERE clause to the query with parameter to be filled.
     * @param tableName         Which table to search.
     * @param columnName        Which column to search.
     * @return String A string ready to be set as a prepared statement.
     */
    private String buildSelectQuery(boolean isSpecifiedSearch, String tableName, String columnName) {
        StringBuilder searchQuery = new StringBuilder();
        searchQuery.append("SELECT ");
        for (int i = 0; i < fileReader.getTableColumnCount(); i++) {
            searchQuery.append(fileReader.getColumnNames().get(i));
            if (i < fileReader.getTableColumnCount() - 1) {
                searchQuery.append(", ");
            }
        }
        searchQuery.append("\n").append("FROM ").append(tableName);
        if (isSpecifiedSearch) {
            searchQuery.append("\n").append("WHERE ").append(columnName).append(" = ?");
        }
        searchQuery.append(";");

        return searchQuery.toString();
    }

    /**
     * Dynamic creation of result based on information from table file and ResultSet
     *
     * @param resultSet ResultSet for the query.
     * @return A prepared string to print out.
     * @throws SQLException          If unable to get column count, or build a ResultHeader based on file.
     * @throws FileNotFoundException If unable to find file.
     */
    private String resultStringBuilder(ResultSet resultSet) throws SQLException, FileNotFoundException {
        String[] rowResult = new String[getColumnCountOfTable(fileReader.getTableName())];
        StringBuilder result = new StringBuilder();
        result.append(getResultHeader(fileReader.getTableName()));
        while (resultSet.next()) {
            for (int i = 1; i <= getColumnCountOfTable(fileReader.getTableName()); i++) {
                rowResult[i - 1] = resultSet.getObject(i).toString();
                if (i == getColumnCountOfTable(fileReader.getTableName())) {

                    result.append("\n");
                }
            }
            result.append(String.format(getResultFormat(), rowResult));
        }
        return result.toString();
    }

    /**
     * Gets all rows based on table name
     *
     * @param tableName Which table to get rows from
     * @return A string ready to be printed.
     * @throws FileNotFoundException If unable to find file with table information.
     * @throws SQLException          If unable to get connection or run query.
     */
    public String getAllRowsByTableName(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String result = "";
        String query = buildSelectQuery(false, tableName, null);

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            result += resultStringBuilder(resultSet);
        }
        return result;
    }

    /**
     * Creates a format for the result to be printed out on based on the MetaData.
     *
     * @return String ready for a String.Format print.
     * @throws SQLException If unable to get connection.
     */
    private String getResultFormat() throws SQLException {
        StringBuilder resultFormat = new StringBuilder();
        ArrayList<String> maxLengthOfColumn = getMaxLengthOfColumnsByTableName();
        for (int i = 0; i < fileReader.getTableColumnCount(); i++) {
            resultFormat.append("%-").append(maxLengthOfColumn.get(i)).append("s | ");
        }

        return resultFormat.toString();
    }

    /**
     * To get the max length that a column should be for the result format.
     *
     * @return ArrayList with the maximum length a column should be.
     * @throws SQLException If unable to get connection.
     */
    private ArrayList<String> getMaxLengthOfColumnsByTableName() throws SQLException {
        ArrayList<String> formatLengthForAllColumns = new ArrayList<>();
        ArrayList<String> displayNames = fileReader.getDisplayNames();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createMaxLengthSelect())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (int i = 0; i < fileReader.getTableColumnCount(); i++) {
                    String maxLength = resultSet.getObject(i + 1).toString();
                    if (Integer.parseInt(maxLength) < displayNames.get(i).length()) {
                        maxLength = "" + displayNames.get(i).length();
                    }
                    formatLengthForAllColumns.add(maxLength);
                }
            }
        }
        return formatLengthForAllColumns;
    }

    /**
     * Creates a statement to get the max length.
     * Helper method for getMaxLengthOfColumnsByTableName()
     *
     * @return String A query to be used in method getMaxLengthOfColumnsByTableName
     */
    private String createMaxLengthSelect() {
        StringBuilder query = new StringBuilder("SELECT ");
        //Build bigger select and run that query, take all results into an array
        for (int i = 0; i < fileReader.getTableColumnCount(); i++) {
            query.append("max(length(").append(fileReader.getColumnNames().get(i)).append("))");
            if (i < fileReader.getTableColumnCount() - 1) {
                query.append(", ");
            }
        }
        query.append("\nFROM ").append(fileReader.getTableName()).append(";");
        return query.toString();
    }

    /**
     * Used to get ResultSetMetaData for the entire table to shorten the rest of methods.
     *
     * @param tableName Which table to get ResultSetMetaData from
     * @return ResultSetMetaData for specified table.
     * @throws SQLException If unable to get connection.
     */
    private ResultSetMetaData getResultSetMetaDataForEntireTable(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + ";";
        ResultSetMetaData resultSetMetaData;

        try (Connection connection = this.databaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            resultSetMetaData = rs.getMetaData();
        }
        return resultSetMetaData;
    }

    /**
     * Used to drop table from database.
     *
     * @param tableName Which table to drop.
     * @throws SQLException If unable to get connection.
     */
    private void dropTable(String tableName) throws SQLException {
        try (Connection connection = databaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName);
        }
    }

    /**
     * Creates database based on the database name in the properties file.
     *
     * @throws SQLException If unable to get connection.
     * @throws IOException  If unable to get access to the properties file.
     */
    private void createDatabase() throws SQLException, IOException {
        try (Connection connection = databaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            String query = "CREATE SCHEMA IF NOT EXISTS " + getDatabaseNameFromProperties() + ";";
            stmt.executeUpdate(query);
        }
    }

    /**
     * Creates a table in the database if it does not exist, based on MetaData from corresponding table file.
     *
     * @param tableName Which table to create.
     * @throws FileNotFoundException If unable to locate table file.
     * @throws SQLException          If unable to connect to the database.
     */
    private void createTableFromMetaData(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));

        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + fileReader.getTableName() + "(\n");

        try (Connection connection = databaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            for (int i = 0; i < fileReader.getTableColumnCount(); i++) {
                createTableQuery.append(fileReader.getColumnNames().get(i));
                createTableQuery.append(" ");
                createTableQuery.append(fileReader.getColumnSQLValues().get(i));

                if (i < fileReader.getTableColumnCount() - 1) {
                    createTableQuery.append(",\n");
                }

                if (i == fileReader.getTableColumnCount() - 1) {
                    if (fileReader.getAmountOfPrimaryKeys() > 0) {
                        createTableQuery.append(",\n");
                        createTableQuery.append(addPrimaryKeyToQuery(i));
                    }
                    if (fileReader.getAmountOfForeignKeys() > 0) {
                        addForeignKeyToList(i);
                    }
                }
            }
            createTableQuery.append(");");
            statement.executeUpdate(createTableQuery.toString());
        }
    }

    /**
     * Helping for createTableFromMetaData to add all the primary keys to the query.
     *
     * @param indexInSQLValueArrayList At which index in the ArrayList to start.
     * @return String A string to be added to the query.
     */
    private String addPrimaryKeyToQuery(int indexInSQLValueArrayList) {
        StringBuilder primaryKeysToBeAddedToQuery = new StringBuilder();
        primaryKeysToBeAddedToQuery.append("PRIMARY KEY(");
        for (int i = 1; i < fileReader.getAmountOfPrimaryKeys() + 1; i++) {
            primaryKeysToBeAddedToQuery.append(fileReader.getColumnSQLValues().get(indexInSQLValueArrayList + i));
            if (i < fileReader.getAmountOfPrimaryKeys()) {
                primaryKeysToBeAddedToQuery.append(", ");
            }
        }
        primaryKeysToBeAddedToQuery.append(")");
        return primaryKeysToBeAddedToQuery.toString();
    }

    /**
     * Finds the foreign keys, if any, creates a query for each one,
     * and adds them to an ArrayList so that they can be added after all tables has been created.
     *
     * @param indexInSQLValueArrayList At which index in the ArrayList to start.
     */
    private void addForeignKeyToList(int indexInSQLValueArrayList) {
        StringBuilder foreignKeyToBeAddedToQuery = new StringBuilder();
        for (int i = 1; i < fileReader.getAmountOfForeignKeys() + 1; i++) {
            foreignKeyToBeAddedToQuery.append("ALTER TABLE ").append(fileReader.getTableName()).append("\n");
            foreignKeyToBeAddedToQuery.append("ADD FOREIGN KEY (");
            foreignKeyToBeAddedToQuery.append(fileReader.getColumnSQLValues().get(indexInSQLValueArrayList + fileReader.getAmountOfPrimaryKeys() + i));
            foreignKeyToBeAddedToQuery.append(") REFERENCES ");
            foreignKeyToBeAddedToQuery.append(fileReader.getColumnSQLValues().get(indexInSQLValueArrayList + fileReader.getAmountOfPrimaryKeys() + i + 1));
            foreignKeyToBeAddedToQuery.append(";");
            foreignKeysToBeAdded.add(foreignKeyToBeAddedToQuery.toString());
        }
    }

    /**
     * Gets the header to be put at top of results that are printed.
     *
     * @param tableName Which table to get result for.
     * @return String A String to be added to a print.
     * @throws FileNotFoundException If unable to find file.
     * @throws SQLException          If unable to get connection.
     */
    private String getResultHeader(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String[] columnDisplayNames = new String[fileReader.getTableColumnCount()];
        for (int i = 0; i < columnDisplayNames.length; i++) {
            columnDisplayNames[i] = fileReader.getDisplayNames().get(i);
        }
        return String.format(getResultFormat(), columnDisplayNames);
    }

    /**
     * Gets the database name from the properties file.
     *
     * @return String Database name
     * @throws IOException If unable to find properties file.
     */
    private String getDatabaseNameFromProperties() throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(getPropertyFilePath());

        properties.load(input);

        return properties.getProperty("databaseName");
    }

    /**
     * Initializes the database with the properties file.
     *
     * @throws IOException If unable to find file.
     */
    public void startConnection() throws IOException {
        databaseConnection.initializeProperties(getPropertyFilePath());
    }

    private String getPropertyFilePath() {
        return propertyFilePath;
    }

    protected void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }


}