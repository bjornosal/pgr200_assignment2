import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;
    private String propertyFilePath;

    public DatabaseConnection() throws IOException {
        setPropertyFilePath("./src/files/defaultDatabaseLogin.properties");
        setUpDatabase();
    }

    public void setUpDatabase() throws IOException {
        dataSource = new MysqlDataSource();

        Properties properties = new Properties();
        InputStream input = new FileInputStream(getPropertyFilePath());

        properties.load(input);

        dataSource.setServerName(properties.getProperty("serverName"));
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        dataSource.setUser(properties.getProperty("databaseUser"));
        dataSource.setPassword(properties.getProperty("databasePassword"));
    }

    public void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }

    public String getPropertyFilePath() {
        return propertyFilePath;
    }

    public MysqlDataSource getDataSource() {
        return dataSource;
    }
}