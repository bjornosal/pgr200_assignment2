import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.IOException;
import java.sql.*;

public class DatabaseHandler{

    private DatabaseConnection databaseConnection;
    private String propertyFilePath;

    public DatabaseHandler() throws IOException {
        databaseConnection = new DatabaseConnection();
        setPropertyFilePath("");
    }

    //TODO remember to change, this is only for test purposes
    public ResultSetMetaData getFullResultSetMetaData(String tableName) throws SQLException, IOException {
        String query = "SELECT * FROM " + tableName + ";";
        ResultSetMetaData rsmd;

        databaseConnection.setUpDatabase(getPropertyFilePath());
        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
        }
        System.out.println("###########METADATA###############");
        return rsmd;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public String getPropertyFilePath() {
        return propertyFilePath;
    }

    public void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }

}