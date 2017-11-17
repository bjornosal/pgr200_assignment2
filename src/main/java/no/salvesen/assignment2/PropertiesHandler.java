package no.salvesen.assignment2;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.util.Properties;

public class PropertiesHandler {

    private String propertyFilePath;

    private boolean isSessionFile;

    public PropertiesHandler() {
        isSessionFile = false;
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

    protected void setDatabaseNameInProperties(String databaseName) throws IOException {
        Properties properties = new Properties();
        String currentServerName;
        String currentUser;
        String currentPassword;

        try(InputStream input = new FileInputStream(propertyFilePath)) {
            properties.load(input);
            currentServerName = properties.getProperty("serverName");
            currentUser = properties.getProperty("databaseUser");
            currentPassword = properties.getProperty("databasePassword");
        }

        try(InputStream input = new FileInputStream(propertyFilePath);
            FileOutputStream fileOut = new FileOutputStream(propertyFilePath)) {
            properties.load(input);

            properties.setProperty("databaseName", databaseName);
            properties.setProperty("serverName", currentServerName);
            properties.setProperty("databaseUser",currentUser);
            properties.setProperty("databasePassword",currentPassword);

            properties.store(fileOut, "Added by user.");
        }
    }

    public void clearPropertiesFile() throws IOException {
        Properties properties = new Properties();
        try(InputStream input = new FileInputStream(propertyFilePath);
            FileOutputStream fileOut = new FileOutputStream(propertyFilePath)) {

            properties.load(input);
            properties.remove("databaseName");
            properties.remove("serverName");
            properties.remove("databaseUser");
            properties.remove("databasePassword");
            properties.store(fileOut, "Deleted by user");

        }

    }

    public void setSessionFile(boolean isSessionFile) {
        this.isSessionFile = isSessionFile;
    }

    public boolean isSessionFile() {
        return isSessionFile;
    }
}
