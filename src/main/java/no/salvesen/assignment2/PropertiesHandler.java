package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {

    private String propertyFilePath;

    private ExceptionHandler exceptionHandler;

    public PropertiesHandler() {
        exceptionHandler = new ExceptionHandler();
    }

    public void initializeProperties(MysqlDataSource dataSource) throws IOException {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(propertyFilePath)) {

            properties.load(input);

            dataSource.setServerName(properties.getProperty("serverName"));
            dataSource.setUser(properties.getProperty("databaseUser"));
            dataSource.setPassword(properties.getProperty("databasePassword"));
        }
    }
    public String getPropertyFilePath() {
        return propertyFilePath;
    }

    public void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }

    public String getDatabaseNameFromProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(propertyFilePath)) {
            properties.load(input);
            return properties.getProperty("databaseName");
        }
    }

   public void waitUntilFileIsClosed() {
       try {
           Thread.sleep(3000);
       } catch (InterruptedException e) {
           System.out.println("Wait was interrupted.");
       }
   }
}
