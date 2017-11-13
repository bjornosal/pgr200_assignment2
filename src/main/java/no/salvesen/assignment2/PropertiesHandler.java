package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesHandler {

    private String propertyFilePath;

    private ExceptionHandler exceptionHandler;
    private boolean isSessionOnlyProperties;

    public PropertiesHandler() {
        exceptionHandler = new ExceptionHandler();
        isSessionOnlyProperties = false;
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

    public void deleteSessionPropertiesForClient() throws IOException {
        if(isSessionOnlyProperties()) {
            Files.delete(Paths.get(propertyFilePath));
        }
    }

    public boolean isSessionOnlyProperties() {
        return isSessionOnlyProperties;
    }

    public void setSessionOnlyProperties(boolean sessionOnlyProperties) {
        isSessionOnlyProperties = sessionOnlyProperties;
    }
}
