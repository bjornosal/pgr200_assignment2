package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public boolean fileIsFinishedSettingUp() throws IOException {
        if(isSessionOnlyProperties()) {
            File sessionPropertiesFile = new File(propertyFilePath);
            return sessionPropertiesFile.exists();
        }
        return true;
    }

    public boolean isSessionOnlyProperties() {
        return isSessionOnlyProperties;
    }

    public void setSessionOnlyProperties(boolean sessionOnlyProperties) {
        isSessionOnlyProperties = sessionOnlyProperties;
    }
}
