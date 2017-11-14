package no.salvesen.assignment2;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrintFormatHandler {

    FileReader fileReader;

    public PrintFormatHandler(FileReader fileReader) {
        this.fileReader = fileReader;
    }


    /**
     * Dynamic creation of result based on information from table file and ResultSet
     *
     * @param resultSet          the result set
     * @param columnCount        the column count
     * @param maxLengthPerColumn the max length per column
     * @return the string
     * @throws SQLException          the sql exception
     * @throws FileNotFoundException the file not found exception
     */
    public String resultStringBuilder(ResultSet resultSet, int columnCount, ArrayList<String> maxLengthPerColumn) throws SQLException, FileNotFoundException {
        String[] rowResult = new String[columnCount];
        StringBuilder result = new StringBuilder();
        result.append(getResultHeader(fileReader.getTableName(), maxLengthPerColumn));
        while(resultSet.next()) {
            for(int i = 1; i <= columnCount; i++) {
                rowResult[i - 1] = resultSet.getObject(i).toString();
                if (i == columnCount) {
                    result.append("\n");
                }
            }
            result.append(String.format(getResultFormat(maxLengthPerColumn),rowResult));
        }
        return result.toString();
    }

    /**
     * Creates a format for the result to be printed out on based on the MetaData.
     * @return String ready for a String.Format print.
     * @param maxLengthPerColumn max length of each column
     * @throws SQLException If unable to get connection.
     */
    private String getResultFormat(ArrayList<String> maxLengthPerColumn) throws SQLException {
        StringBuilder resultFormat = new StringBuilder();
        for(int i = 0; i < fileReader.getTableColumnCount(); i++) {
            resultFormat.append("%-").append(maxLengthPerColumn.get(i)).append("s | ");
        }
        return resultFormat.toString();
    }

    /**
     * Gets the header to be put at top of results that are printed.
     * @param tableName table to get header for
     * @param maxLengthPerColumn max length per column
     * @return header for results
     * @throws FileNotFoundException if no file
     * @throws SQLException if issue with query
     */
    private String getResultHeader(String tableName, ArrayList<String> maxLengthPerColumn) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String[] columnDisplayNames = new String[fileReader.getTableColumnCount()];
        for (int i = 0; i < columnDisplayNames.length; i++) {
            columnDisplayNames[i] = fileReader.getDisplayNames().get(i);
        }
        return String.format(getResultFormat(maxLengthPerColumn), columnDisplayNames);
    }

}
