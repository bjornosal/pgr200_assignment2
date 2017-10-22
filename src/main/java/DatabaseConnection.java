import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;

    public DatabaseConnection( ) throws IOException {
        dataSource = new MysqlDataSource();

    }

    public void setUpDatabase(String propertyFilePath) throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertyFilePath);

        properties.load(input);

        dataSource.setServerName(properties.getProperty("serverName"));
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        dataSource.setUser(properties.getProperty("databaseUser"));
        dataSource.setPassword(properties.getProperty("databasePassword"));
    }


    public MysqlDataSource getDataSource() {
        return dataSource;
    }
}