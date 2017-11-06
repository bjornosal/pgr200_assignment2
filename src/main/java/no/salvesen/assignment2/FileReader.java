package no.salvesen.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    private File subjectFile;
    private File roomFile;
    private File lecturerFile;

    private String tableName;
    private int tableColumnCount;
    private int amountOfPrimaryKeys;
    private int amountOfForeignKeys;

    private ArrayList<String> columnNames;
    private ArrayList<String> columnSQLValues;
    private ArrayList<String> displayNames;
    private ArrayList<String> insertionValues;

    /**
     * Constructor for class FileReader.
     * Initializes files at corresponding paths for the three tables.
     */
    public FileReader() {
        setSubjectFile(new File("src/files/database files/subject.csv"));
        setRoomFile(new File("src/files/database files/room.csv"));
        setLecturerFile(new File("src/files/database files/lecturer.csv"));
    }

    /**
     * Initializes the ArrayLists to keep all the values
     * Makes use of other methods to place information from file in corresponding ArrayLists.
     *
     * @param tableFile File containing table information.
     * @throws FileNotFoundException If not file is found.
     */
    public void readFile(File tableFile) throws FileNotFoundException {
        columnNames = new ArrayList<>();
        columnSQLValues = new ArrayList<>();
        displayNames = new ArrayList<>();
        insertionValues = new ArrayList<>();

        Scanner fileParser = new Scanner(tableFile);
        fileParser.useDelimiter(";|\\r\\n|\\n");

        readMetaDataFromFile(fileParser);
        readDisplayNamesFromFile(fileParser);
        readInsertValuesFromFile(fileParser);
    }


    /**
     * Description of file:
     * First line: tableName;columnCount;primaryKeys;foreignKeys;ForeignKeyReferences(Table(column))
     * Second line: columnName * columnCount
     * Third line: MySQL values * columnCount
     * Fourth line: Display Names * columnCount
     * Fifth -> lines: Insertion values
     */

    /**
     * Reads the SQL metadata from the selected file.
     *
     * @param fileParser Scanner to use to parse the file.
     */
    private void readMetaDataFromFile(Scanner fileParser) {

        setTableName(fileParser.next());
        setTableColumnCount(fileParser.nextInt());
        setAmountOfPrimaryKeys(fileParser.nextInt());
        setAmountOfForeignKeys(fileParser.nextInt());


        for(int i = 0; i < getTableColumnCount(); i++) {
            columnNames.add(fileParser.next());
        }
        //The part of the file that contains the primary keys and foreign keys.
        for(int i = 0; i < getTableColumnCount() + getAmountOfPrimaryKeys() + (getAmountOfForeignKeys()*2); i++) {
            columnSQLValues.add(fileParser.next());
        }
    }

    /**
     * Reads the names that should be displayed when printing a table.
     * @param fileParser Scanner to use to parse the file.
     */
    private void readDisplayNamesFromFile(Scanner fileParser) {
        for(int i = 0; i < getTableColumnCount(); i++) {
            displayNames.add(fileParser.next());
        }
    }

    /**
     * Reads the values that will go into the insert statements
     * @param fileParser Scanner to use to parse the file.
     */
    private void readInsertValuesFromFile(Scanner fileParser) {
        while(fileParser.hasNext()) {
            for (int i = 0; i < getTableColumnCount(); i++) {
                insertionValues.add(fileParser.next());
            }
        }
    }

    /**
     * Returns a table file.
     * @param tableName Name of table that one wants the file for.
     * @return The file with the name of param
     */
    public File getFileByTableName(String tableName) {
        tableName = tableName.toLowerCase();
        switch(tableName) {
            case "subject":
                return getSubjectFile();
            case "room":
                return getRoomFile();
            case "lecturer":
                return getLecturerFile();
        }
        return null;
    }

    private File getSubjectFile() {
        return subjectFile;
    }

    public void setSubjectFile(File subjectFile) {
        this.subjectFile = subjectFile;
    }

    private File getRoomFile() {
        return roomFile;
    }

    public void setRoomFile(File roomFile) {
        this.roomFile = roomFile;
    }

    private File getLecturerFile() {
        return lecturerFile;
    }

    public void setLecturerFile(File lecturerFile) {
        this.lecturerFile = lecturerFile;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTableColumnCount() {
        return tableColumnCount;
    }

    private void setTableColumnCount(int tableColumnCount) {
        this.tableColumnCount = tableColumnCount;
    }

    public int getAmountOfPrimaryKeys() {
        return amountOfPrimaryKeys;
    }

    private void setAmountOfPrimaryKeys(int amountOfPrimaryKeys) {
        this.amountOfPrimaryKeys = amountOfPrimaryKeys;
    }

    public int getAmountOfForeignKeys() {
        return amountOfForeignKeys;
    }

    private void setAmountOfForeignKeys(int amountOfForeignKeys) {
        this.amountOfForeignKeys = amountOfForeignKeys;
    }

    public ArrayList<String> getColumnSQLValues() {
        return columnSQLValues;
    }

    public ArrayList<String> getDisplayNames() {
        return displayNames;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public ArrayList<String> getInsertionValues() {
        return insertionValues;
    }
}