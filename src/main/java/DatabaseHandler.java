import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.IOException;
import java.sql.*;

public class DatabaseHandler{

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    public DatabaseHandler() throws IOException {

    }

    //TODO remember to change, this is only for test purposes
    public ResultSetMetaData getFullResultSetMetaData(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + ";";
        ResultSetMetaData rsmd;

        MysqlDataSource dataSource = databaseConnection.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
        }

        System.out.println("###########METADATA###############");
        return rsmd;
    }
}